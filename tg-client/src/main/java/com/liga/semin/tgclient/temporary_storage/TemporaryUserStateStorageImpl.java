package com.liga.semin.tgclient.temporary_storage;

import com.liga.semin.tgclient.bot_api.BotState;
import com.liga.semin.tgclient.model.UserDto;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class TemporaryUserStateStorageImpl implements TemporaryUserStateStorage {
    private final Map<Long, BotState> botStates = new HashMap<>();
    private final Map<Long, UserDto> usersProfiles = new HashMap<>();

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
        return usersProfiles.containsKey(userId);
    }


    @Override
    public UserDto getUser(Long userId) {
        usersProfiles.putIfAbsent(userId, new UserDto());
        return usersProfiles.get(userId);
    }

    @Override
    public void resetUser(Long userId) {
        botStates.put(userId, BotState.INIT);
        usersProfiles.put(userId, new UserDto());
        usersProfiles.get(userId).setId(userId);
    }

    @Override
    public void removeUserFromTemporaryStorage(Long userId) {
        usersProfiles.remove(userId);
    }
}
