package com.liga.semin.tgclient.util;

import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Статический класс для сокращения получения id-шников
 */
public class UpdateProcessor {

    public static long getUserId(Update update) {
        return update.getMessage().getFrom().getId();
    }

    public static String getChatId(Update update) {
        return String.valueOf(update.getMessage().getChatId());
    }

    public static String getAnswer(Update update) {
        return update.getMessage().getText();
    }
}
