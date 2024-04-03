package com.liga.semin.tgclient.bot_api.message_handlers.profile;

import com.liga.semin.tgclient.bot_api.BotState;
import com.liga.semin.tgclient.bot_api.message_handlers.MessageHandler;
import com.liga.semin.tgclient.external_service.ExternalServerService;
import com.liga.semin.tgclient.keyboard.ReplyProfileKeyboardMarker;
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
public class UserProfileHandlerImpl implements MessageHandler {
    private final BotState handlerState = BotState.MY_PROFILE;
    private final TemporaryUserStateStorage tmpStorage;
    private final ReplyProfileKeyboardMarker replyProfileKeyboard;
    private final ExternalServerService externalServerService;
    @Override
    public BotApiMethod<?> handleUpdate(Update update) {
        var chatId = UpdateProcessor.getChatId(update);
        var userId = UpdateProcessor.getUserId(update);

        tmpStorage.setState(userId, BotState.MAIN_MENU); // след. этап - гл. меню
        UserDto user = externalServerService.getUserById(userId); // todo: пока так, потом - запрос на сервер - профиль в виде картинки;
        String profile =  String.format(""" 
                Ваша анкета:
                Вы: %s,
                Вас величают: %s
                Ваше описание: %s
                Кого вы ищите: %s
                """,
                user.getGender().getGender(), user.getUsername(), user.getDescription(), user.getMateGender().getGender()
        );

        SendMessage reply = new SendMessage(chatId, profile);
        reply.setReplyMarkup(replyProfileKeyboard.getProfileKeyboard());
        return reply;
    }

    @Override
    public BotState getHanlderState() {
        return handlerState;
    }
}
