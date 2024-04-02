package com.liga.semin.tgclient.bot_api.message_handlers;

import com.liga.semin.tgclient.bot_api.BotState;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface MessageHandler {
    public BotApiMethod<?> handleUpdate(Update update);

    BotState getHanlderState();
}
