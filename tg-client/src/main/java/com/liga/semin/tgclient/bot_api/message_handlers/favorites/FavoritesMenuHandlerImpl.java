package com.liga.semin.tgclient.bot_api.message_handlers.favorites;

import com.liga.semin.tgclient.bot_api.BotState;
import com.liga.semin.tgclient.bot_api.message_handlers.MessageHandler;
import com.liga.semin.tgclient.keyboard.ReplyFavoritesKeyboardMarker;
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
public class FavoritesMenuHandlerImpl implements MessageHandler {
    private final BotState handlerState = BotState.FAVORITES_MENU;
    private final TemporaryUserStateStorage tmpStorage;
    private final ReplyFavoritesKeyboardMarker replyFavoritesKeyboardMarker;
    private static final Logger logger = LoggerFactory.getLogger(FavoritesMenuHandlerImpl.class);


    @Override
    public List<PartialBotApiMethod<?>> handleUpdate(Update update) {
        var chatId = UpdateProcessor.getChatId(update);
        var userId = UpdateProcessor.getUserId(update);
        logger.debug("User {} is on favorite menu, handling update with state {}", userId, handlerState);

        tmpStorage.setState(userId, BotState.CHOOSING_FAVORITES_MENU);
        SendMessage reply = new SendMessage(chatId, "Выберите, кого хотите посмотреть");
        reply.setReplyMarkup(replyFavoritesKeyboardMarker.getFavoritesMenuKeyboard());
        logger.debug("Waiting for user {} choosing type of showing favorites", userId);

        return List.of(reply);
    }

    @Override
    public BotState getHandlerState() {
        return handlerState;
    }
}
