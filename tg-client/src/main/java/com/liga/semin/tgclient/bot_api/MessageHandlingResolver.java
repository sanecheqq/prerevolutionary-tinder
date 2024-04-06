package com.liga.semin.tgclient.bot_api;

import com.liga.semin.tgclient.bot_api.message_handlers.MessageHandler;
import com.liga.semin.tgclient.util.UpdateProcessor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Класс-resolver для выбора дальнейшего handler-а обработки ответа пользователя на основании выбранного состояния бота
 * в классе UserStateResolver
 */
@Component
@RequiredArgsConstructor
public class MessageHandlingResolver {
    private final Map<BotState, MessageHandler> handlers;
    private static final Logger logger = LoggerFactory.getLogger(MessageHandlingResolver.class);

    @Autowired
    public MessageHandlingResolver(List<MessageHandler> handlersList) {
        handlers = handlersList.stream().collect(Collectors.toMap(MessageHandler::getHandlerState, v -> v));
    }

    public List<PartialBotApiMethod<?>> resolveHandler(BotState state, Update update) {
        logger.debug("Resolving MessageHandler for user {} and state {}", UpdateProcessor.getUserId(update), state);
        return handlers.get(state).handleUpdate(update);
    }
}
