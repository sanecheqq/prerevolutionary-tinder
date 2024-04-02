package com.liga.semin.tgclient.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Класс для временного хранения данных юзера в процессе регистрации, а также для отправки запросов на сервер.
 */
@Getter
@Setter
public class UserDto {
    private long id;
    private GenderType userGender;
    private String username;
    private String description;
    private GenderType mateGender;
}
