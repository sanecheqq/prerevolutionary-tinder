package com.liga.semin.server.message;


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
    private String gender;
    @JsonProperty("mate_gender")
    private String mateGender;
}

