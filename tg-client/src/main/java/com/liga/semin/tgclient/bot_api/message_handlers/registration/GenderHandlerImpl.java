package com.liga.semin.tgclient.bot_api.message_handlers.registration;

import com.liga.semin.tgclient.bot_api.BotState;
import com.liga.semin.tgclient.bot_api.message_handlers.MessageHandler;
import com.liga.semin.tgclient.keyboard.ReplyRegistrationKeyboardMarker;
import com.liga.semin.tgclient.temporary_storage.TemporaryUserStateStorage;
import com.liga.semin.tgclient.util.UpdateProcessor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GenderHandlerImpl implements MessageHandler {
    private final BotState handlerState = BotState.SET_GENDER;
    private final TemporaryUserStateStorage tmpStorage;
    private final ReplyRegistrationKeyboardMarker replyRegistrationKeyboardMarker;
    private static final Logger logger = LoggerFactory.getLogger(GenderHandlerImpl.class);

    @Override
    public List<PartialBotApiMethod<?>> handleUpdate(Update update) {
        var chatId = UpdateProcessor.getChatId(update);
        var userId = UpdateProcessor.getUserId(update);
        logger.debug("User {} is on gender choosing, handling update with state {}", userId, handlerState);

        SendMessage reply = new SendMessage(chatId, "Вы сударъ или сударыня?");
        reply.setReplyMarkup(replyRegistrationKeyboardMarker.getGenderKeyboard());

        tmpStorage.setState(userId, BotState.SET_NAME); // ставим состояние следующего шага
        logger.debug("Waiting for user {} choosing gender", userId);
        return List.of(reply);
    }

    @Override
    public BotState getHandlerState() {
        return handlerState;
    }
}
