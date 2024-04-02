package com.liga.semin.tgclient.temporary_storage;

import com.liga.semin.tgclient.bot_api.BotState;

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
}
