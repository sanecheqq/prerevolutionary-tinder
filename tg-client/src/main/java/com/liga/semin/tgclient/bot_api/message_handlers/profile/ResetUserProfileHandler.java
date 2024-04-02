package com.liga.semin.tgclient.bot_api.message_handlers.profile;

import com.liga.semin.tgclient.bot_api.BotState;
import com.liga.semin.tgclient.bot_api.message_handlers.MessageHandler;
import com.liga.semin.tgclient.keyboard.ReplyRegistrationKeyboardMarker;
import com.liga.semin.tgclient.temporary_storage.TemporaryUserStateStorage;
import com.liga.semin.tgclient.util.UpdateProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class ResetUserProfileHandler implements MessageHandler {
    private final BotState handlerState = BotState.RESET;
    private final TemporaryUserStateStorage tmpStorage;
    private final ReplyRegistrationKeyboardMarker replyRegistrationKeyboard;

    @Override
    public BotApiMethod<?> handleUpdate(Update update) {
        var chatId = UpdateProcessor.getChatId(update);
        var userId = UpdateProcessor.getUserId(update);

        tmpStorage.removeUser(userId);
        SendMessage reply = new SendMessage(chatId, "Данные вашего профиля сброшены за исключением ваших любимцев. Зарегистрируйтесь заново");
        reply.setReplyMarkup(replyRegistrationKeyboard.getRegistrationKeyboard());
        return reply;
    }

    @Override
    public BotState getHanlderState() {
        return handlerState;
    }

}
