package com.liga.semin.tgclient.bot_api;

import com.liga.semin.tgclient.temporary_storage.TemporaryUserStateStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class UserStateResolver {
    private final TemporaryUserStateStorage tmpStorage;
    private final MessageHandlingResolver handlingResolver;
    private Map<String, BotState> userStates;

    {
        userStates = Map.of(
                "/start", BotState.INIT,
                "Зарегистрироваться", BotState.SET_GENDER,
                "Анкета", BotState.MY_PROFILE,
                "Любимцы", BotState.LIKES,
                "Поиск", BotState.SEARCHING
        );
    }

    public BotApiMethod<?> resolveUser(Update update) {
        Message message = update.getMessage();
        Long userId = message.getFrom().getId();
        String userText = getText(update);
        BotState botState = userStates.getOrDefault(userText, tmpStorage.getState(userId));
        tmpStorage.setState(userId, botState);
        return handlingResolver.resolveHandler(botState, update);
    }

    private String getText(Update update) {
        String text = "я пустота";
        if (update.hasCallbackQuery()) {
            text = update.getCallbackQuery().getMessage().getText();
        } else if (update.hasMessage() && update.getMessage().hasText()) {
            text = update.getMessage().getText().trim();
        }
        return text;
    }
}
