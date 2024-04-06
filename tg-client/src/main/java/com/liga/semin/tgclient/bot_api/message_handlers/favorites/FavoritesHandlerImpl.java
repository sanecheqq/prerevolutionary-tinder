package com.liga.semin.tgclient.bot_api.message_handlers.favorites;

import com.liga.semin.tgclient.bot_api.BotState;
import com.liga.semin.tgclient.bot_api.message_handlers.MessageHandler;
import com.liga.semin.tgclient.external_service.message.ProfileDto;
import com.liga.semin.tgclient.keyboard.ReplyFavoritesKeyboardMarker;
import com.liga.semin.tgclient.temporary_storage.TemporaryFavoritesStorageImpl;
import com.liga.semin.tgclient.temporary_storage.TemporaryUserStateStorage;
import com.liga.semin.tgclient.util.UpdateProcessor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FavoritesHandlerImpl implements MessageHandler {
    private final BotState handlerState = BotState.FAVORITES_ACTIVE;
    private final TemporaryUserStateStorage tmpStorage;
    private final TemporaryFavoritesStorageImpl favoritesStorage;
    private final ReplyFavoritesKeyboardMarker favoritesKeyboardMarker;
    private static final Logger logger = LoggerFactory.getLogger(FavoritesHandlerImpl.class);

    @Override
    public List<PartialBotApiMethod<?>> handleUpdate(Update update) {
        var chatId = UpdateProcessor.getChatId(update);
        var userId = UpdateProcessor.getUserId(update);
        var answer = UpdateProcessor.getAnswer(update);
        logger.debug("User {} is on looking favorites / followers / mutual list, handling update with state {}", userId, handlerState);

        tmpStorage.setState(userId, handlerState);

        List<ProfileDto> profiles = favoritesStorage.getProfiles(userId);
        if (profiles.isEmpty()) {
            SendMessage reply = new SendMessage(chatId, "К сожалению, здесь никого нет");
            reply.setReplyMarkup(favoritesKeyboardMarker.getIteratingFavoritesKeyboard());
            logger.debug("Showing list is empty for user {}", userId);
            return List.of(reply);
        }
        int offset = favoritesStorage.getOffset(userId);
        offset += switch (answer) {
            case "Вперед" -> +1;
            case "Назад" -> -1;
            default -> 0;
        };
        if (offset >= profiles.size())
            offset = 0;
        else if (offset < 0)
            offset = profiles.size() - 1;
        favoritesStorage.putOffset(userId, offset);

        ProfileDto profileDto = profiles.get(offset);
        SendPhoto reply = new SendPhoto();
        reply.setChatId(chatId);
        reply.setCaption(profileDto.caption());
        InputStream is = new ByteArrayInputStream(profileDto.image());
        reply.setPhoto(new InputFile(is, "profile.png"));
        reply.setReplyMarkup(favoritesKeyboardMarker.getIteratingFavoritesKeyboard());
        logger.debug("Showing for user {} profile of user {}", userId, profileDto.caption());

        return List.of(reply);
    }

    @Override
    public BotState getHandlerState() {
        return handlerState;
    }
}
