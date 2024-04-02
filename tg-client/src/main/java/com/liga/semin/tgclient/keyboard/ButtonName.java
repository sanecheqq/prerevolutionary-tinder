package com.liga.semin.tgclient.keyboard;

import lombok.Getter;

@Getter
public enum ButtonName {
    REGISTER("Зарегистрироваться"),
    MALE("Сударъ"),
    FEMALE("Сударыня"),
    GO_TO_MAIN_MENU("Перейти в главное меню");

    private String name;
    private ButtonName(String name) {
        this.name = name;
    }
}
