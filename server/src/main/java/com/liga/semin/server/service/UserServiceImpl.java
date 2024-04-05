package com.liga.semin.server.service;

import com.liga.semin.server.exception.UserNotFoundException;
import com.liga.semin.server.message.GetUserProfileResponse;
import com.liga.semin.server.message.UserDto;
import com.liga.semin.server.model.GenderType;
import com.liga.semin.server.model.User;
import com.liga.semin.server.repository.UserRepository;
import com.liga.semin.server.util.converter.UserToUserDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;


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
        user.setOffset(0);
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
    public boolean postFavorite(Long from, Long to) {
        User fromUser = findOrElseThrow(from);
        User toUser = findOrElseThrow(to);
        fromUser.getFavorites().add(toUser);
        userRepository.save(fromUser);
        return fromUser.getFollowers().contains(toUser); // проверяем, взаимный ли like
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
        int offset = user.getOffset();
        List<User> list = userRepository.findNextWithOffset(id, user.getGender(), user.getMateGender(), GenderType.ALL, PageRequest.of(offset, 1));
        if (list.isEmpty()) { // если стал offset > кол-во пользаков, то вернется пустой список, поэтому начинаем по кругу заново.
            offset = 0;
            list = userRepository.findNextWithOffset(id, user.getGender(), user.getMateGender(), GenderType.ALL, PageRequest.of(offset, 1));
        }
        if (list.isEmpty()) { // если лист снова пустой, то значит никого не нашли
            return null;
        }
        User nextWithOffset = list.get(0);
        user.setOffset(offset+1);
        userRepository.save(user);
        return new GetUserProfileResponse(
                nextWithOffset.getId(),
                nextWithOffset.getName(),
                nextWithOffset.getGender().getGender(),
                imageProcessingService.putProfileOnImage(nextWithOffset.getDescription())
        );
    }

    private User findOrElseThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.valueOf(id)));
    }

}
