package com.liga.semin.tgclient.external_service;

import com.liga.semin.tgclient.external_service.message.GetProfilesResponse;
import com.liga.semin.tgclient.external_service.message.GetUserProfileResponse;
import com.liga.semin.tgclient.external_service.message.PostFavoriteResponse;
import com.liga.semin.tgclient.model.UserDto;

public interface ExternalServerService {
    UserDto getUserById(Long userId);

    GetUserProfileResponse getUserProfileById(Long userId);

    GetUserProfileResponse getNextSearchingUserProfileById(Long userId);

    UserDto postUser(UserDto userDto);

    PostFavoriteResponse postFavorite(long from, long to);

    GetProfilesResponse getFavoriteProfiles(long from);

    GetProfilesResponse getFollowerProfiles(long from);

    GetProfilesResponse getMutualFollowingProfiles(long from);
}
