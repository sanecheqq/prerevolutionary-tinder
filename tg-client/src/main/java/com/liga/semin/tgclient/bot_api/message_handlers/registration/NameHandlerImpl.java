package com.liga.semin.tgclient.bot_api.message_handlers.registration;

import com.liga.semin.tgclient.bot_api.BotState;
import com.liga.semin.tgclient.bot_api.message_handlers.MessageHandler;
import com.liga.semin.tgclient.keyboard.ReplyRegistrationKeyboardMarker;
import com.liga.semin.tgclient.model.GenderType;
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
public class NameHandlerImpl implements MessageHandler {
    private final BotState handlerState = BotState.SET_NAME;
    private final TemporaryUserStateStorage tmpStorage;
    private final ReplyRegistrationKeyboardMarker replyRegistrationKeyboardMarker;
    private static final Logger logger = LoggerFactory.getLogger(RegistrationFinishedHandlerImpl.class);

    @Override
    public List<PartialBotApiMethod<?>> handleUpdate(Update update) {
        var chatId = UpdateProcessor.getChatId(update);
        var userId = UpdateProcessor.getUserId(update);
        logger.debug("User {} is on writing his name, handling update with state {}", userId, handlerState);

        tmpStorage.getUser(userId).setGender(GenderType.getGenderType(UpdateProcessor.getAnswer(update)));
        logger.debug("Saving chosen gender by user {}", userId);

        tmpStorage.setState(userId, BotState.SET_DESCRIPTION); // след. этап - добавление описания.
        SendMessage reply = new SendMessage(String.valueOf(chatId), "Как вас величать?");
        reply.setReplyMarkup(replyRegistrationKeyboardMarker.removeReplyKeyboard());
        logger.debug("Waiting for user {} name", userId);

        return List.of(reply);
    }

    @Override
    public BotState getHandlerState() {
        return handlerState;
    }
}
