package com.liga.semin.tgclient.bot_api.message_handlers.profile;

import com.liga.semin.tgclient.bot_api.BotState;
import com.liga.semin.tgclient.bot_api.message_handlers.MessageHandler;
import com.liga.semin.tgclient.external_service.ExternalServerService;
import com.liga.semin.tgclient.external_service.message.GetUserProfileResponse;
import com.liga.semin.tgclient.keyboard.ReplyProfileKeyboardMarker;
import com.liga.semin.tgclient.temporary_storage.TemporaryUserStateStorage;
import com.liga.semin.tgclient.util.UpdateProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class UserProfileHandlerImpl implements MessageHandler {
    private final BotState handlerState = BotState.MY_PROFILE;
    private final TemporaryUserStateStorage tmpStorage;
    private final ReplyProfileKeyboardMarker replyProfileKeyboard;
    private final ExternalServerService externalServerService;

    @Override
    public PartialBotApiMethod<?> handleUpdate(Update update) {
        var chatId = UpdateProcessor.getChatId(update);
        var userId = UpdateProcessor.getUserId(update);

        tmpStorage.setState(userId, BotState.MAIN_MENU); // след. этап - гл. меню

        GetUserProfileResponse profileResponse = externalServerService.getUserProfileById(userId);
        byte[] image = profileResponse.image();
        InputStream is = new ByteArrayInputStream(image);

        SendPhoto reply = new SendPhoto();
        reply.setChatId(chatId);
        reply.setPhoto(new InputFile(is, ""));
        reply.setCaption(profileResponse.gender() + ", " + profileResponse.name());
        reply.setReplyMarkup(replyProfileKeyboard.getProfileKeyboard());
        return reply;
    }

    @Override
    public BotState getHandlerState() {
        return handlerState;
    }
}
