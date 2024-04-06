package com.liga.semin.tgclient.external_service;

import com.liga.semin.tgclient.external_service.message.GetProfilesResponse;
import com.liga.semin.tgclient.external_service.message.GetUserProfileResponse;
import com.liga.semin.tgclient.external_service.message.PostFavoriteResponse;
import com.liga.semin.tgclient.model.UserDto;

/**
 * Сервис для взаимодействия с внешним сервером
 */
public interface ExternalServerService {
    /**
     * Метод для получения DTO пользователя из сервера по id
     * @param userId идентификатор пользователя
     * @return DTO, если пользователь найден, иначе - null
     */
    UserDto getUserById(Long userId);

    /**
     * Метод для получения профиля пользователя в виде картинки и подписи по id
     * @param userId идентификатор пользователя
     * @return response-класс в случае успеха, иначе при ошибках на сервере - null
     */
    GetUserProfileResponse getUserProfileById(Long userId);

    /**
     * Метод для получения следующего профиля для показа пользователя во время поиска
     * @param userId идентификатор пользователя
     * @return response-класс в случае успеха, при отсутствии пользователей для показа - null
     */
    GetUserProfileResponse getNextSearchingUserProfileById(Long userId);

    /**
     * Метод для сохранения пользователя после пройденной регистрации
     * @param userDto DTO класс профиля пользователя
     * @return сохраненный DTO класс
     */
    UserDto postUser(UserDto userDto);

    /**
     * Метод для сохранения "лайка" от одного пользователя другому
     * @param from идентификатор пользователя, который сделал "лайк"
     * @param to идентификатор пользователя, которого "лайкнули"
     * @return response-класс, содержащий информацию о том, является ли лайк взаимным
     */
    PostFavoriteResponse postFavorite(long from, long to);

    /**
     * Метод для получения списка избранных пользователем профилей
     * @param from идентификатор пользователя, который получает список
     * @return response-класс со списком профилей
     */
    GetProfilesResponse getFavoriteProfiles(long from);

    /**
     * Метод для получения списка пользователей, которые "лайкнули" текущего пользователя
     * @param from идентификатор пользователя, который получает список
     * @return response-класс со списком профилей
     */
    GetProfilesResponse getFollowerProfiles(long from);

    /**
     * Метод для получения списка пользователей, с которыми у текущего пользователя произошла взаимная симпатия
     * @param from идентификатор пользователя, который получает список
     * @return response-класс со списком профилей
     */
    GetProfilesResponse getMutualFollowingProfiles(long from);
}
