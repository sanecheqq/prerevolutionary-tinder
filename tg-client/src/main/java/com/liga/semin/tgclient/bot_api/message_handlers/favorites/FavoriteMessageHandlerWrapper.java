package com.liga.semin.tgclient.bot_api.message_handlers.favorites;

import com.liga.semin.tgclient.bot_api.BotState;
import com.liga.semin.tgclient.bot_api.message_handlers.MessageHandler;
import com.liga.semin.tgclient.external_service.ExternalServerService;
import com.liga.semin.tgclient.temporary_storage.TemporaryFavoritesStorage;
import com.liga.semin.tgclient.util.UpdateProcessor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FavoriteMessageHandlerWrapper implements MessageHandler {
    private final BotState handlerState = BotState.CHOOSING_FAVORITES_MENU;
    private final TemporaryFavoritesStorage favoritesStorage;
    private final ExternalServerService externalServerService;
    private final FavoritesHandlerImpl favoritesHandler;
    private static final Logger logger = LoggerFactory.getLogger(FavoriteMessageHandlerWrapper.class);

    @Override
    public List<PartialBotApiMethod<?>> handleUpdate(Update update) {
        var answer = UpdateProcessor.getAnswer(update);
        var userId = UpdateProcessor.getUserId(update);
        logger.debug("User {} is on waiting for list of favorites, handling update with state {}", userId, handlerState);
        logger.debug("Upload list from server...");
        var response = switch (answer) {
            case "Понравились Вам" -> externalServerService.getFavoriteProfiles(userId);
            case "Выбрали Вас" -> externalServerService.getFollowerProfiles(userId);
            case "Взаимный выбор" -> externalServerService.getMutualFollowingProfiles(userId);
            default -> externalServerService.getFavoriteProfiles(userId);
        };
        favoritesStorage.addProfiles(userId, response.profiles());

        logger.debug("Showing list for user {}", userId);
        return favoritesHandler.handleUpdate(update);
    }

    @Override
    public BotState getHandlerState() {
        return handlerState;
    }
}
