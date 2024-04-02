package com.liga.semin.tgclient.bot_api.message_handlers.registration;

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
public class GenderHandlerImpl implements MessageHandler {
    private final BotState handlerState = BotState.SET_GENDER;
    private final TemporaryUserStateStorage tmpStorage;
    private final ReplyRegistrationKeyboardMarker replyRegistrationKeyboardMarker;

    @Override
    public BotApiMethod<?> handleUpdate(Update update) {
        var chatId = UpdateProcessor.getChatId(update);
        var userId = UpdateProcessor.getUserId(update);

        SendMessage reply = new SendMessage(chatId, "Вы сударъ или сударыня?");
        reply.setReplyMarkup(replyRegistrationKeyboardMarker.getGenderKeyboard());
        tmpStorage.setState(userId, BotState.SET_NAME); // ставим состояние следующего шага
        return reply;
    }

    @Override
    public BotState getHanlderState() {
        return handlerState;
    }
}
