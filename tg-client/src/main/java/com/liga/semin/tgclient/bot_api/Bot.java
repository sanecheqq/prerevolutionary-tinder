package com.liga.semin.tgclient.bot_api;

import com.liga.semin.tgclient.bot_config.BotConfig;
import com.liga.semin.tgclient.util.UpdateProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Component
public class Bot extends TelegramLongPollingBot {
    private final BotConfig botConfig;
    private final UserStateResolver userStateResolver;
    private static final Logger logger = LoggerFactory.getLogger(Bot.class);


    @Autowired
    public Bot(BotConfig botConfig, UserStateResolver userStateResolver) {
        super(botConfig.getToken());
        this.botConfig = botConfig;
        this.userStateResolver = userStateResolver;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public void onUpdateReceived(Update update) {
        var userId = UpdateProcessor.getUserId(update);
        var chatId = UpdateProcessor.getChatId(update);
        logger.info("Got update from user {}, chatID {}", userId, chatId);
        List<PartialBotApiMethod<?>> replies = userStateResolver.resolveUser(update);
        for (var reply : replies) {
            try {
                if (reply instanceof SendPhoto) {
                    logger.info("Sending reply Photo from user {}, chatID {}", userId, chatId);
                    execute((SendPhoto) reply);
                } else {
                    logger.info("Sending reply Message from user {}, chatID {}", userId, chatId);
                    execute((SendMessage) reply);
                }
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
