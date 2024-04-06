package com.liga.semin.tgclient.bot_api.message_handlers.profile;

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
public class ResetUserProfileHandler implements MessageHandler {
    private final BotState handlerState = BotState.RESET;
    private final TemporaryUserStateStorage tmpStorage;
    private final ReplyRegistrationKeyboardMarker replyRegistrationKeyboard;
    private static final Logger logger = LoggerFactory.getLogger(ResetUserProfileHandler.class);

    @Override
    public List<PartialBotApiMethod<?>> handleUpdate(Update update) {
        var chatId = UpdateProcessor.getChatId(update);
        var userId = UpdateProcessor.getUserId(update);
        logger.debug("User {} is on resetting his profile, handling update with state {}", userId, handlerState);

        tmpStorage.resetUser(userId);
        SendMessage reply = new SendMessage(chatId, "Данные вашего профиля сброшены вместе с вашими любимцами. Зарегистрируйтесь заново");
        reply.setReplyMarkup(replyRegistrationKeyboard.getRegistrationKeyboard());
        logger.debug("User {} profile is dropped. Registration is started", userId);
        return List.of(reply);
    }

    @Override
    public BotState getHandlerState() {
        return handlerState;
    }

}
