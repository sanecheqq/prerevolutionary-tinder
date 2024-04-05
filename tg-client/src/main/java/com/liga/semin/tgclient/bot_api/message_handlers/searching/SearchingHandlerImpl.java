package com.liga.semin.tgclient.bot_api.message_handlers.searching;

import com.liga.semin.tgclient.bot_api.BotState;
import com.liga.semin.tgclient.bot_api.message_handlers.MessageHandler;
import com.liga.semin.tgclient.external_service.ExternalServerService;
import com.liga.semin.tgclient.external_service.message.GetUserProfileResponse;
import com.liga.semin.tgclient.keyboard.ReplySearchingKeyboardMarker;
import com.liga.semin.tgclient.temporary_storage.TemporaryUserStateStorage;
import com.liga.semin.tgclient.util.UpdateProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class SearchingHandlerImpl implements MessageHandler {
    private final BotState handlerState = BotState.SEARCHING;
    private final TemporaryUserStateStorage tmpStorage;
    private final ReplySearchingKeyboardMarker replySearchingKeyboardMarker;
    private final ExternalServerService externalServerService;

    @Override
    public PartialBotApiMethod<?> handleUpdate(Update update) {
        var chatId = UpdateProcessor.getChatId(update);
        var userId = UpdateProcessor.getUserId(update);

        tmpStorage.setState(userId, BotState.SEARCHING); // след. этап - не изменяется

        GetUserProfileResponse profileResponse = externalServerService.getNextSearchingUserProfileById(userId);
        if (profileResponse == null) { // значит, никого не нашли.
            SendMessage reply = new SendMessage(chatId, "К сожалению, на данный момент для Вас никого предложить не можем");
            reply.setReplyMarkup(replySearchingKeyboardMarker.getSearchingKeyboard());
            return reply;
        }
        byte[] image = profileResponse.image();
        InputStream is = new ByteArrayInputStream(image);

        SendPhoto reply = new SendPhoto();
        reply.setChatId(chatId);
        reply.setPhoto(new InputFile(is, "profile.png"));
        reply.setCaption(profileResponse.gender() + ", " + profileResponse.name());
        reply.setReplyMarkup(replySearchingKeyboardMarker.getSearchingKeyboard());
        return reply;
    }

    @Override
    public BotState getHandlerState() {
        return handlerState;
    }
}
