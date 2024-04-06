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
public class MateGenderHandlerImpl implements MessageHandler {
    private final BotState handlerState = BotState.SET_MATE_GENDER;
    private final TemporaryUserStateStorage tmpStorage;
    private final ReplyRegistrationKeyboardMarker replyRegistrationKeyboardMarker;
    private static final Logger logger = LoggerFactory.getLogger(MateGenderHandlerImpl.class);

    @Override
    public List<PartialBotApiMethod<?>> handleUpdate(Update update) {
        var chatId = UpdateProcessor.getChatId(update);
        var userId = UpdateProcessor.getUserId(update);
        logger.debug("User {} is on mate's gender choosing, handling update with state {}", userId, handlerState);

        tmpStorage.getUser(userId).setDescription(UpdateProcessor.getAnswer(update).trim());
        logger.debug("Saving written by user {} description", userId);
        tmpStorage.setState(userId, BotState.REGISTRATION_FINISHED); // след. этап - конец регистрации

        SendMessage reply = new SendMessage(chatId, "Кого вы ищите?");
        reply.setReplyMarkup(replyRegistrationKeyboardMarker.getMateGenderKeyboard());
        logger.debug("Waiting for user {} choosing mate gender", userId);
        return List.of(reply);
    }

    @Override
    public BotState getHandlerState() {
        return handlerState;
    }
}
