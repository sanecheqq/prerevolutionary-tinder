package com.liga.semin.tgclient.bot_api.message_handlers;

import com.liga.semin.tgclient.bot_api.BotState;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface MessageHandler {
    PartialBotApiMethod<?> handleUpdate(Update update);

    BotState getHandlerState();
}
