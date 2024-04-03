package com.liga.semin.tgclient.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Класс для временного хранения данных юзера в процессе регистрации, а также для отправки запросов на сервер.
 */
@Getter
@Setter
@ToString
public class UserDto {
    private long id;
    private String username;
    private String description;
    private GenderType gender;
    @JsonProperty("mate_gender")
    private GenderType mateGender;
}
