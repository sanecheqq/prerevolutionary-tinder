package com.liga.semin.tgclient.bot_api.message_handlers.registration;

import com.liga.semin.tgclient.bot_api.BotState;
import com.liga.semin.tgclient.bot_api.message_handlers.MessageHandler;
import com.liga.semin.tgclient.keyboard.ReplyRegistrationKeyboardMarker;
import com.liga.semin.tgclient.model.UserDto;
import com.liga.semin.tgclient.temporary_storage.TemporaryUserStateStorage;
import com.liga.semin.tgclient.util.UpdateProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class StartHandlerImpl implements MessageHandler {
    private final BotState handlerState = BotState.INIT;
    private final TemporaryUserStateStorage tmpStorage;
    private final ReplyRegistrationKeyboardMarker replyRegistrationKeyboard;

    @Override
    public BotApiMethod<?> handleUpdate(Update update) {
        var chatId = UpdateProcessor.getChatId(update);
        var userId = UpdateProcessor.getUserId(update);

        UserDto userDto = null; // externalServer.findUserById(userId); // todo: полагаю, что здесь необходимо будет чекнуть пользака на сервере;
        SendMessage reply;
        if (userDto != null) {
            reply = new SendMessage(chatId, "Вы уже зарегистрированы. Переходите в главное меню");
            reply.setReplyMarkup(replyRegistrationKeyboard.getGoToMainMenuKeyboard());
        } else if (tmpStorage.containsUser(userId)) { // это если пользак прописал /start во время/после регистрации
            reply = new SendMessage(chatId, "Вы уже начали, не торопитесь");
            reply.setReplyMarkup(replyRegistrationKeyboard.removeReplyKeyboard());
        } else {// пользака нет - регистрируем
            tmpStorage.getUser(userId).setId(userId);
            reply = new SendMessage(chatId, "Вам нужно зарегистрироваться");
            reply.setReplyMarkup(replyRegistrationKeyboard.getRegistrationKeyboard());
        }
        return reply;
    }

    @Override
    public BotState getHanlderState() {
        return handlerState;
    }
}
