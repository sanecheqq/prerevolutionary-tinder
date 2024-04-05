package com.liga.semin.tgclient.external_service;

import com.liga.semin.tgclient.external_service.message.GetProfilesResponse;
import com.liga.semin.tgclient.external_service.message.GetUserProfileResponse;
import com.liga.semin.tgclient.external_service.message.PostFavoriteResponse;
import com.liga.semin.tgclient.model.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class ExternalServerServiceImpl implements ExternalServerService {
    @Value("${server_host}")
    private String serverHost;
    private final RestTemplate restTemplate;

    @Override
    public UserDto getUserById(Long userId) {
        URI getUserUri = URI.create(serverHost).resolve("/api/v1/users/" + userId);
        try {
            return restTemplate.getForObject(getUserUri, UserDto.class);
        } catch (HttpClientErrorException e) {
            return null;
        }
    }

    @Override
    public GetUserProfileResponse getUserProfileById(Long userId) {
        URI getUserUri = URI.create(serverHost).resolve("/api/v1/users/profile/" + userId);
        try {
            return restTemplate.getForObject(getUserUri, GetUserProfileResponse.class);
        } catch (HttpClientErrorException e) {
            return null;
        }
    }

    @Override
    public GetUserProfileResponse getNextSearchingUserProfileById(Long userId) {
        URI getUserUri = URI.create(serverHost).resolve("/api/v1/users/searching/" + userId);
        try {
            return restTemplate.getForObject(getUserUri, GetUserProfileResponse.class);
        } catch (HttpClientErrorException e) {
            return null;
        }
    }

    @Override
    public UserDto postUser(UserDto userDto) {
        URI postUserUri = URI.create(serverHost).resolve("/api/v1/users");
        return restTemplate.postForObject(postUserUri, userDto, UserDto.class);
    }

    @Override
    public PostFavoriteResponse postFavorite(long from, long to) {
        URI postFavoriteUri = URI.create(serverHost).resolve("/api/v1/users/favorite");
        String urlTemplate = UriComponentsBuilder.fromHttpUrl(postFavoriteUri.toString())
                .queryParam("from", from)
                .queryParam("to", to)
                .encode()
                .toUriString();
        try {
            return restTemplate.postForObject(urlTemplate, null, PostFavoriteResponse.class);
        } catch (HttpClientErrorException e) {
            return null;
        }
    }

    @Override
    public GetProfilesResponse getFavoriteProfiles(long from) {
        URI getFavorites = URI.create(serverHost).resolve("/api/v1/users/favorite/" + from);
        try {
            return restTemplate.getForObject(getFavorites, GetProfilesResponse.class);
        } catch (HttpClientErrorException e) {
            return null;
        }
    }

    @Override
    public GetProfilesResponse getFollowerProfiles(long from) {
        URI getFavorites = URI.create(serverHost).resolve("/api/v1/users/follower/" + from);
        try {
            return restTemplate.getForObject(getFavorites, GetProfilesResponse.class);
        } catch (HttpClientErrorException e) {
            return null;
        }
    }

    @Override
    public GetProfilesResponse getMutualFollowingProfiles(long from) {
        URI getFavorites = URI.create(serverHost).resolve("/api/v1/users/mutual/" + from);
        try {
            return restTemplate.getForObject(getFavorites, GetProfilesResponse.class);
        } catch (HttpClientErrorException e) {
            return null;
        }
    }
}
