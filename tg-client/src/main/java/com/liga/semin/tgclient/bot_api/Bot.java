package com.liga.semin.tgclient.bot_api;

import com.liga.semin.tgclient.bot_config.BotConfig;
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
        List<PartialBotApiMethod<?>> replies = userStateResolver.resolveUser(update);
        for (var reply : replies) {
            try {
                if (reply instanceof SendPhoto) {
                    execute((SendPhoto) reply);
                } else {
                    execute((SendMessage) reply);
                }
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
