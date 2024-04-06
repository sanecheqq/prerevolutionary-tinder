package com.liga.semin.tgclient.bot_api.message_handlers.registration;

import com.liga.semin.tgclient.bot_api.BotState;
import com.liga.semin.tgclient.bot_api.message_handlers.MessageHandler;
import com.liga.semin.tgclient.external_service.ExternalServerService;
import com.liga.semin.tgclient.keyboard.ReplyRegistrationKeyboardMarker;
import com.liga.semin.tgclient.model.UserDto;
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
public class StartHandlerImpl implements MessageHandler {
    private final BotState handlerState = BotState.INIT;
    private final TemporaryUserStateStorage tmpStorage;
    private final ReplyRegistrationKeyboardMarker replyRegistrationKeyboard;
    private final ExternalServerService externalServerService;
    private static final Logger logger = LoggerFactory.getLogger(StartHandlerImpl.class);


    @Override
    public List<PartialBotApiMethod<?>> handleUpdate(Update update) {
        var chatId = UpdateProcessor.getChatId(update);
        var userId = UpdateProcessor.getUserId(update);
        logger.debug("User {} is on /start, handling update with state {}", userId, handlerState);

        UserDto userDto = externalServerService.getUserById(userId);
        SendMessage reply;
        if (userDto != null) {
            logger.debug("User {} exists. Redirecting to main menu", userId);
            reply = new SendMessage(chatId, "Вы уже зарегистрированы. Переходите в главное меню");
            reply.setReplyMarkup(replyRegistrationKeyboard.getGoToMainMenuKeyboard());
        } else if (tmpStorage.containsUser(userId)) { // это если пользак прописал /start во время/после регистрации
            logger.debug("User {} in registration process", userId);
            reply = new SendMessage(chatId, "Вы уже начали, не торопитесь");
            reply.setReplyMarkup(replyRegistrationKeyboard.removeReplyKeyboard());
        } else {// пользака нет - регистрируем
            logger.debug("User {} doesnt exist. Registration process is started", userId);
            tmpStorage.getUser(userId).setId(userId);
            reply = new SendMessage(chatId, "Вам нужно зарегистрироваться");
            reply.setReplyMarkup(replyRegistrationKeyboard.getRegistrationKeyboard());
            logger.debug("Wait for user click {} button", userId);

        }
        return List.of(reply);
    }

    @Override
    public BotState getHandlerState() {
        return handlerState;
    }
}
