package com.liga.semin.tgclient.temporary_storage;

import com.liga.semin.tgclient.bot_api.BotState;
import com.liga.semin.tgclient.model.UserDto;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class TemporaryUserStateStorageImpl implements TemporaryUserStateStorage {
    private final Map<Long, BotState> botStates = new HashMap<>();
    private final Map<Long, UserDto> usersDtos = new HashMap<>();

    @Override
    public BotState getState(Long userId) {
        return botStates.getOrDefault(userId, BotState.INIT);
    }


    @Override
    public void setState(Long userId, BotState botState) {
        botStates.put(userId, botState);
    }

    @Override
    public boolean containsUser(Long userId) {
        return usersDtos.containsKey(userId);
    }


    @Override
    public UserDto getUser(Long userId) {
        usersDtos.putIfAbsent(userId, new UserDto());
        return usersDtos.get(userId);
    }

    @Override
    public void resetUser(Long userId) {
        botStates.put(userId, BotState.INIT);
        usersDtos.put(userId, new UserDto());
        usersDtos.get(userId).setId(userId);
    }

    @Override
    public void removeUserFromTemporaryStorage(Long userId) {
        usersDtos.remove(userId);
    }
}
