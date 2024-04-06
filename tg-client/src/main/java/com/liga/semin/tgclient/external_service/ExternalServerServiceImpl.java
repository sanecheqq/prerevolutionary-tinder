package com.liga.semin.tgclient.external_service;

import com.liga.semin.tgclient.external_service.message.GetProfilesResponse;
import com.liga.semin.tgclient.external_service.message.GetUserProfileResponse;
import com.liga.semin.tgclient.external_service.message.PostFavoriteResponse;
import com.liga.semin.tgclient.model.UserDto;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(ExternalServerServiceImpl.class);


    @Override
    public UserDto getUserById(Long userId) {
        logger.debug("external: getUserById {}", userId);
        URI getUserUri = URI.create(serverHost).resolve("/api/v1/users/" + userId);
        try {
            return restTemplate.getForObject(getUserUri, UserDto.class);
        } catch (HttpClientErrorException e) {
            return null;
        }
    }

    @Override
    public GetUserProfileResponse getUserProfileById(Long userId) {
        logger.debug("external: getUserProfileById {}", userId);
        URI getUserUri = URI.create(serverHost).resolve("/api/v1/users/profile/" + userId);
        try {
            return restTemplate.getForObject(getUserUri, GetUserProfileResponse.class);
        } catch (HttpClientErrorException e) {
            return null;
        }
    }

    @Override
    public GetUserProfileResponse getNextSearchingUserProfileById(Long userId) {
        logger.debug("external: getNextSearchingUserProfileById {}", userId);
        URI getUserUri = URI.create(serverHost).resolve("/api/v1/users/searching/" + userId);
        try {
            return restTemplate.getForObject(getUserUri, GetUserProfileResponse.class);
        } catch (HttpClientErrorException e) {
            return null;
        }
    }

    @Override
    public UserDto postUser(UserDto userDto) {
        logger.debug("external: postUser id {}, name {}", userDto.getId(), userDto.getUsername());
        URI postUserUri = URI.create(serverHost).resolve("/api/v1/users");
        return restTemplate.postForObject(postUserUri, userDto, UserDto.class);
    }

    @Override
    public PostFavoriteResponse postFavorite(long from, long to) {
        logger.debug("external: postFavorite from {} to {}", from, to);
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
        logger.debug("external: getFavoriteProfiles from {}", from);
        URI getFavorites = URI.create(serverHost).resolve("/api/v1/users/favorite/" + from);
        try {
            return restTemplate.getForObject(getFavorites, GetProfilesResponse.class);
        } catch (HttpClientErrorException e) {
            return null;
        }
    }

    @Override
    public GetProfilesResponse getFollowerProfiles(long from) {
        logger.debug("external: getFollowerProfiles from {}", from);
        URI getFavorites = URI.create(serverHost).resolve("/api/v1/users/follower/" + from);
        try {
            return restTemplate.getForObject(getFavorites, GetProfilesResponse.class);
        } catch (HttpClientErrorException e) {
            return null;
        }
    }

    @Override
    public GetProfilesResponse getMutualFollowingProfiles(long from) {
        logger.debug("external: getMutualFollowingProfiles from {}", from);
        URI getFavorites = URI.create(serverHost).resolve("/api/v1/users/mutual/" + from);
        try {
            return restTemplate.getForObject(getFavorites, GetProfilesResponse.class);
        } catch (HttpClientErrorException e) {
            return null;
        }
    }
}
