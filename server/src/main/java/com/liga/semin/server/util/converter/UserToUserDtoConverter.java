package com.liga.semin.server.util.converter;

import com.liga.semin.server.message.UserDto;
import com.liga.semin.server.model.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDtoConverter implements Converter<User, UserDto> {
    @Override
    public UserDto convert(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getName());
        userDto.setDescription(user.getDescription());
        userDto.setGender(user.getGender().toString());
        userDto.setMateGender(user.getMateGender().toString());
        return userDto;
    }
}
