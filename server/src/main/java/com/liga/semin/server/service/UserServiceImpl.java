package com.liga.semin.server.service;

import com.liga.semin.server.exception.UserNotFoundException;
import com.liga.semin.server.message.*;
import com.liga.semin.server.model.GenderType;
import com.liga.semin.server.model.User;
import com.liga.semin.server.repository.UserRepository;
import com.liga.semin.server.util.converter.UserToUserDtoConverter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserToUserDtoConverter userToUserDtoConverter;
    private final ImageProcessingService imageProcessingService;
    private final TranslatorService translatorService;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public UserDto createUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setName(translatorService.translateTextToOldRussian(userDto.getUsername()));
        var description = userDto.getDescription().substring(0, Math.min(300, userDto.getDescription().length()));
        int firstLineEnd = description.indexOf("\n");
        if (firstLineEnd != -1) {
            String firstLineTranslated = translatorService.translateTextToOldRussian(description.substring(0, firstLineEnd));
            description = translatorService.translateTextToOldRussian(description.substring(firstLineEnd+1));
            description = firstLineTranslated + "\n" + description;
        } else {
            description = translatorService.translateTextToOldRussian(description);
        }
        user.setDescription(description);
        user.setGender(GenderType.valueOf(userDto.getGender()));
        user.setMateGender(GenderType.valueOf(userDto.getMateGender()));
        user.setSearchOffset(0);
        logger.debug("Creating user with id {} and name {}", user.getId(), user.getName());
        return userToUserDtoConverter.convert(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        logger.debug("Deleting user with id {}", id);
        userRepository.deleteById(id);
    }

    @Override
    public UserDto getUser(Long id) {
        logger.debug("Getting user dto with id {}", id);
        return userToUserDtoConverter.convert(findOrElseThrow(id));
    }

    @Override
    public PostFavoriteResponse postFavorite(Long from, Long to) {
        logger.debug("Posting favorite from user {} to user {}", from, to);

        User fromUser = findOrElseThrow(from);
        User toUser = findOrElseThrow(to);
        fromUser.getFavorites().add(toUser);
        userRepository.save(fromUser);
        return new PostFavoriteResponse(fromUser.getFollowers().contains(toUser)); // проверяем, взаимный ли like
    }

    @Override
    public GetUserProfileResponse getUserProfile(Long id) {
        logger.debug("Getting user profile with id {}", id);
        User user = findOrElseThrow(id);
        return new GetUserProfileResponse(
                user.getId(),
                user.getName(),
                user.getGender().getGender(),
                imageProcessingService.putProfileOnImage(user.getDescription())
        );
    }

    @Override
    public GetUserProfileResponse getNextUserProfileWithOffset(Long id) {
        logger.debug("Getting next user profile for searching by user with id {}", id);

        User user = findOrElseThrow(id);
        int offset = user.getSearchOffset();
        List<User> list = userRepository.findNextWithOffset(id, user.getGender(), user.getMateGender(), GenderType.ALL, PageRequest.of(offset, 1));
        if (list.isEmpty()) { // если стал offset > кол-во пользаков, то вернется пустой список, поэтому начинаем по кругу заново.
            offset = 0;
            list = userRepository.findNextWithOffset(id, user.getGender(), user.getMateGender(), GenderType.ALL, PageRequest.of(offset, 1));
        }
        if (list.isEmpty()) { // если лист снова пустой, то значит никого не нашли
            return null;
        }
        User nextWithOffset = list.get(0);
        logger.debug("Got next user {} with offset {}", id, offset);

        user.setSearchOffset(offset+1);
        userRepository.save(user);
        return new GetUserProfileResponse(
                nextWithOffset.getId(),
                nextWithOffset.getName(),
                nextWithOffset.getGender().getGender(),
                imageProcessingService.putProfileOnImage(nextWithOffset.getDescription())
        );
    }

    @Override
    public GetProfilesResponse getFavoriteProfiles(Long id) {
        logger.debug("Getting list of favorite profiles by user {}", id);

        Set<User> favorites = findOrElseThrow(id).getFavorites();
        List<ProfileDto> profiles = new ArrayList<>();
        for (var user : favorites) {
            var sb = new StringBuilder();
            profiles.add(new ProfileDto(
                    sb.append(user.getGender().getGender())
                            .append(", ")
                            .append(user.getName())
                            .append(", Любим")
                            .append(user.getGender() == GenderType.MALE ? " " : "а ") // Любим | Любима
                            .append("Вами")
                            .toString(),
                    imageProcessingService.putProfileOnImage(user.getDescription())
            ));
        }
        return new GetProfilesResponse(profiles);
    }

    @Override
    public GetProfilesResponse getFollowerProfiles(Long id) {
        logger.debug("Getting list of followers profiles by user {}", id);

        Set<User> followers = findOrElseThrow(id).getFollowers();
        List<ProfileDto> profiles = new ArrayList<>();
        for (var user : followers) {
            var sb = new StringBuilder();
            profiles.add(new ProfileDto(
                    sb.append(user.getGender().getGender())
                            .append(", ")
                            .append(user.getName())
                            .append(", Вы любимы")
                            .toString(),
                    imageProcessingService.putProfileOnImage(user.getDescription())
            ));
        }
        return new GetProfilesResponse(profiles);
    }

    @Override
    public GetProfilesResponse getMutualFollowingProfiles(Long id) {
        logger.debug("Getting list of mutual following profiles by user {}", id);

        User userById = findOrElseThrow(id);
        Set<User> followers = userById.getFollowers();
        Set<User> favorites = userById.getFavorites();
        List<ProfileDto> profiles = new ArrayList<>();
        for (var user : favorites) {
            if (!followers.contains(user))
                continue;
            var sb = new StringBuilder();
            profiles.add(new ProfileDto(
                    sb.append(user.getGender().getGender())
                            .append(", ")
                            .append(user.getName())
                            .append(", Взаимность")
                            .toString(),
                    imageProcessingService.putProfileOnImage(user.getDescription())
            ));
        }
        return new GetProfilesResponse(profiles);

    }

    private User findOrElseThrow(Long id) {
        logger.debug("Find by id user {} in users-table", id);

        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.valueOf(id)));
    }

}
