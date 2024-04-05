package com.liga.semin.tgclient.external_service;

import com.liga.semin.tgclient.external_service.message.GetUserProfileResponse;
import com.liga.semin.tgclient.model.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

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
    public UserDto postUser(UserDto userDto) {
        URI postUserUri = URI.create(serverHost).resolve("/api/v1/users");
        return restTemplate.postForObject(postUserUri, userDto, UserDto.class);
    }
}
