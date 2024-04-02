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
public class MateGenderHandlerImpl implements MessageHandler {
    private final BotState handlerState = BotState.SET_MATE_GENDER;
    private final TemporaryUserStateStorage tmpStorage;
    private final ReplyRegistrationKeyboardMarker replyRegistrationKeyboardMarker;

    @Override
    public BotApiMethod<?> handleUpdate(Update update) {
        var chatId = UpdateProcessor.getChatId(update);
        var userId = UpdateProcessor.getUserId(update);

        tmpStorage.getUser(userId).setDescription(UpdateProcessor.getAnswer(update));
        tmpStorage.setState(userId, BotState.REGISTRATION_FINISHED); // след. этап - конец регистрации

        SendMessage reply = new SendMessage(chatId, "Кого вы ищите?");
        reply.setReplyMarkup(replyRegistrationKeyboardMarker.getMateGenderKeyboard());
        return reply;
    }

    @Override
    public BotState getHanlderState() {
        return handlerState;
    }
}