package com.liga.semin.tgclient.bot_api;

import com.liga.semin.tgclient.bot_api.message_handlers.MessageHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MessageHandlingResolver {
    private final Map<BotState, MessageHandler> handlers;

    @Autowired
    public MessageHandlingResolver(List<MessageHandler> handlersList) {
        handlers = handlersList.stream().collect(Collectors.toMap(MessageHandler::getHandlerState, v -> v));
    }

    public List<PartialBotApiMethod<?>> resolveHandler(BotState state, Update update) {
        return handlers.get(state).handleUpdate(update);
    }
}
