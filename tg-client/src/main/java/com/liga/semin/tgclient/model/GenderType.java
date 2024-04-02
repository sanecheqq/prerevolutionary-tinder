package com.liga.semin.tgclient.model;

import lombok.Getter;

@Getter
public enum GenderType {
    MALE("Сударъ"),
    FEMALE("Сударыня"),
    ALL("Всех");

    private String gender;
    GenderType(String gender) {
        this.gender = gender;
    }

    public static GenderType getGenderType(String gender) {
        return switch (gender) {
            case "Сударъ" -> MALE;
            case "Сударыня" -> FEMALE;
            case "Всех" -> ALL;
            default -> throw new IllegalArgumentException("Неверный пол");
        };
    }
}
