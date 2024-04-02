package com.liga.semin.tgclient.bot_api;

import com.liga.semin.tgclient.bot_config.BotConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class Bot extends TelegramWebhookBot {
    private final BotConfig botConfig;


    @Autowired
    public Bot(BotConfig botConfig) {
        super(botConfig.getToken());
        this.botConfig = botConfig;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {

        return null;
    }

    @Override
    public String getBotPath() {
        return botConfig.getWebhook();
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }
}
