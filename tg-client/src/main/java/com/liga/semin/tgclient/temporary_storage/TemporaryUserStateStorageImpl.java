package com.liga.semin.tgclient.temporary_storage;

import com.liga.semin.tgclient.bot_api.BotState;
import com.liga.semin.tgclient.model.UserDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class TemporaryUserStateStorageImpl implements TemporaryUserStateStorage {
    private final Map<Long, BotState> botStates = new HashMap<>();
    private final Map<Long, UserDto> usersDtos = new HashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(TemporaryUserStateStorageImpl.class);


    @Override
    public BotState getState(Long userId) {
        logger.debug("Getting state of Bot for user {}", userId);
        return botStates.getOrDefault(userId, BotState.INIT);
    }


    @Override
    public void setState(Long userId, BotState botState) {
        logger.debug("Setting state of Bot for user {}", userId);
        botStates.put(userId, botState);
    }

    @Override
    public boolean containsUser(Long userId) {
        logger.debug("Checking if user {} exists", userId);
        return usersDtos.containsKey(userId);
    }


    @Override
    public UserDto getUser(Long userId) {
        logger.debug("Getting user {} from storage", userId);
        usersDtos.putIfAbsent(userId, new UserDto());
        return usersDtos.get(userId);
    }

    @Override
    public void resetUser(Long userId) {
        logger.debug("Resetting user {} profile and state", userId);
        botStates.put(userId, BotState.INIT);
        usersDtos.put(userId, new UserDto());
        usersDtos.get(userId).setId(userId);
    }

    @Override
    public void removeUserFromTemporaryStorage(Long userId) {
        logger.debug("Removing user {} profile from storage", userId);
        usersDtos.remove(userId);
    }
}
