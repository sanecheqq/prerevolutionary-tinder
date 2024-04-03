package com.liga.semin.tgclient.temporary_storage;

import com.liga.semin.tgclient.bot_api.BotState;
import com.liga.semin.tgclient.model.UserDto;

/**
 * Временное хранилище данных юзера на процесс регистрации.
 */
public interface TemporaryUserStateStorage {
    /**
     * Получение текущего состояния бота для данного пользователя
     * @param userId идентификатор пользователя текущего
     * @return состояние бота
     */
    BotState getState(Long userId);

    /**
     * Установка текущего состояния бота для данного пользователя
     * @param userId идентификатор пользователя
     * @param botState устанавливаемое состояние бота
     */
    void setState(Long userId, BotState botState);

    boolean containsUser(Long userId);

    UserDto getUser(Long userId);

    void resetUser(Long userId);

    void removeUserFromTemporaryStorage(Long userId);
}
