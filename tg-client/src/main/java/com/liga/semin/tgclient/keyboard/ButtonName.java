package com.liga.semin.tgclient.keyboard;

import lombok.Getter;

@Getter
public enum ButtonName {
    REGISTER("Зарегистрироваться"),
    MALE("Сударъ"),
    FEMALE("Сударыня"),
    ALL("Всех"),
    SEARCHING("Поиск"),
    PROFILE("Анкета"),
    LIKES("Любимцы"),
    RESET("Сброс данных профиля"),
    RIGHT("Вправо"),
    LEFT("Влево"),
    FOLLOWERS("Выбрали Вас"),
    FAVORITES("Понравились Вам"),
    MUTUAL("Взаимный выбор"),
    FORWARD("Вперед"),
    BACKWARD("Назад"),
    GO_TO_MAIN_MENU("Перейти в главное меню");

    private String name;

    private ButtonName(String name) {
        this.name = name;
    }
}
