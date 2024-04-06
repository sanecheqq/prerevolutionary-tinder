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

    /**
     * Проверка, существует ли профиль пользователя во временном хранилище
     * @param userId идентификатор проверяемого пользователя
     * @return true, если пользователь есть, иначе - false
     */
    boolean containsUser(Long userId);

    /**
     * Метод для получения текущего состояния профиля пользователя
     * @param userId идентификатор пользователя, чье состояние получаем
     * @return DTO профиля пользователя
     */
    UserDto getUser(Long userId);

    /**
     * Метод для сброса состояния бота и пользовательского профиля во временном хранилище.
     * Используется для осуществления повторной регистрации по желанию пользователя
     * @param userId идентификатор пользователя, который сбрасывает свой профиль
     */
    void resetUser(Long userId);

    /**
     * Метод для удаления профиля пользователя из хранилища, если регистрация была завершена.
     * В дальнейшем профиль пользователя хранится на сервере.
     * @param userId идентификатор удаляемого профиля пользователя
     */
    void removeUserFromTemporaryStorage(Long userId);
}
