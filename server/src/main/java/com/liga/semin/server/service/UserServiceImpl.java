package com.liga.semin.server.service;

import com.liga.semin.server.exception.UserNotFoundException;
import com.liga.semin.server.message.UserDto;
import com.liga.semin.server.model.GenderType;
import com.liga.semin.server.model.User;
import com.liga.semin.server.repository.UserRepository;
import com.liga.semin.server.util.converter.UserToUserDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserToUserDtoConverter userToUserDtoConverter;


    @Override
    public UserDto createUser(UserDto userDto) {
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getUsername());
        user.setDescription(userDto.getDescription());
        user.setGender(GenderType.valueOf(userDto.getGender()));
        user.setMateGender(GenderType.valueOf(userDto.getMateGender()));
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

    private User findOrElseThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(String.valueOf(id)));
    }

}
