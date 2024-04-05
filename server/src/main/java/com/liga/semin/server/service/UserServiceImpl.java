package com.liga.semin.server.service;

import com.liga.semin.server.exception.UserNotFoundException;
import com.liga.semin.server.message.*;
import com.liga.semin.server.model.GenderType;
import com.liga.semin.server.model.User;
import com.liga.semin.server.repository.UserRepository;
import com.liga.semin.server.util.converter.UserToUserDtoConverter;
import lombok.RequiredArgsConstructor;
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

    @Override
    public UserDto createUser(UserDto userDto) {
        // todo: перед сохранением прогонять имя + описание через транслятор
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getUsername());
        user.setDescription(userDto.getDescription().substring(0, Math.min(300, userDto.getDescription().length())));
        user.setGender(GenderType.valueOf(userDto.getGender()));
        user.setMateGender(GenderType.valueOf(userDto.getMateGender()));
        user.setSearchOffset(0);
        return userToUserDtoConverter.convert(userRepository.save(user));
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDto getUser(Long id) {
        return userToUserDtoConverter.convert(findOrElseThrow(id));
    }

    @Override
    public PostFavoriteResponse postFavorite(Long from, Long to) {
        User fromUser = findOrElseThrow(from);
        User toUser = findOrElseThrow(to);
        fromUser.getFavorites().add(toUser);
        userRepository.save(fromUser);
        return new PostFavoriteResponse(fromUser.getFollowers().contains(toUser)); // проверяем, взаимный ли like
    }

    @Override
    public GetUserProfileResponse getUserProfile(Long id) {
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
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.valueOf(id)));
    }

}
