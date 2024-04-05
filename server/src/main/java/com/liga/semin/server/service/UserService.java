package com.liga.semin.server.service;

import com.liga.semin.server.message.GetUserProfileResponse;
import com.liga.semin.server.message.UserDto;

public interface UserService {
    UserDto createUser(UserDto user);

    void deleteUser(Long id);

    UserDto getUser(Long id);

    boolean postFavorite(Long from, Long to);

    GetUserProfileResponse getUserProfile(Long id);
}
