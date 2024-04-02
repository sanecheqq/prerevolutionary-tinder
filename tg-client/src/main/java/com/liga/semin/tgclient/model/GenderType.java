package com.liga.semin.tgclient.model;

public enum GenderType {
    MALE("Сударъ"),
    FEMALE("Сударыня"),
    ALL("Все");

    private String gender;
    GenderType(String gender) {
        this.gender = gender;
    }

    public static GenderType getGenderType(String gender) {
        return switch (gender) {
            case "Сударъ" -> MALE;
            case "Сударыня" -> FEMALE;
            case "Все" -> ALL;
            default -> throw new IllegalArgumentException("Неверный пол");
        };
    }
}
