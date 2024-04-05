package com.liga.semin.server.service;

import com.liga.semin.server.message.*;

public interface UserService {
    UserDto createUser(UserDto user);

    void deleteUser(Long id);

    UserDto getUser(Long id);

    PostFavoriteResponse postFavorite(Long from, Long to);

    GetUserProfileResponse getUserProfile(Long id);

    GetUserProfileResponse getNextUserProfileWithOffset(Long id);

    GetProfilesResponse getFavoriteProfiles(Long id);

    GetProfilesResponse getFollowerProfiles(Long id);

    GetProfilesResponse getMutualFollowingProfiles(Long id);
}
