package com.liga.semin.tgclient.bot_api.message_handlers.registration;

import com.liga.semin.tgclient.bot_api.BotState;
import com.liga.semin.tgclient.bot_api.message_handlers.MessageHandler;
import com.liga.semin.tgclient.external_service.ExternalServerService;
import com.liga.semin.tgclient.external_service.message.GetUserProfileResponse;
import com.liga.semin.tgclient.keyboard.ReplyMainMenuKeyboardMarker;
import com.liga.semin.tgclient.model.GenderType;
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
import java.util.List;

@Component
@RequiredArgsConstructor
public class RegistrationFinishedHandlerImpl implements MessageHandler {
    private final BotState handlerState = BotState.REGISTRATION_FINISHED;
    private final TemporaryUserStateStorage tmpStorage;
    private final ReplyMainMenuKeyboardMarker replyMainKeyboard;
    private final ExternalServerService externalServerService;

    @Override
    public List<PartialBotApiMethod<?>> handleUpdate(Update update) {
        var chatId = UpdateProcessor.getChatId(update);
        var userId = UpdateProcessor.getUserId(update);

        tmpStorage.getUser(userId).setMateGender(GenderType.getGenderType(UpdateProcessor.getAnswer(update)));
        tmpStorage.setState(userId, BotState.MAIN_MENU); // след. этап - пол товарища
        externalServerService.postUser(tmpStorage.getUser(userId));
        tmpStorage.removeUserFromTemporaryStorage(userId); // удаляем пользователя из хранилища, чтобы не засорял, но оставляем в хранилище его состояние

        GetUserProfileResponse profileResponse = externalServerService.getUserProfileById(userId);
        SendPhoto reply = new SendPhoto();
        reply.setChatId(chatId);
        reply.setCaption(profileResponse.gender() + ", " + profileResponse.name());
        InputStream is = new ByteArrayInputStream(profileResponse.image());
        reply.setPhoto(new InputFile(is, "profile.png"));
        reply.setReplyMarkup(replyMainKeyboard.getMainMenuKeyboard());
        return List.of(reply);
    }

    @Override
    public BotState getHandlerState() {
        return handlerState;
    }
}
