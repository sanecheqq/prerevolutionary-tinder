package com.liga.semin.tgclient.external_service;

import com.liga.semin.tgclient.external_service.message.GetUserProfileResponse;
import com.liga.semin.tgclient.model.UserDto;

public interface ExternalServerService {
    UserDto getUserById(Long userId);

    GetUserProfileResponse getUserProfileById(Long userId);

    UserDto postUser(UserDto userDto);
}
