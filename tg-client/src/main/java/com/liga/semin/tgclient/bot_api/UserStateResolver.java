package com.liga.semin.tgclient.bot_api;

import com.liga.semin.tgclient.temporary_storage.TemporaryUserStateStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
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
                "Поиск", BotState.SEARCHING,
                "Вперед", BotState.SEARCHING,
                "Назад", BotState.SEARCHING,
                "Перейти в главное меню", BotState.MAIN_MENU,
                "Сброс данных профиля", BotState.RESET
        );
    }

    public PartialBotApiMethod<?> resolveUser(Update update) {
        Message message = update.getMessage();
        Long userId = message.getFrom().getId();
        String userText = getText(update);
        BotState botState = userStates.getOrDefault(userText, tmpStorage.getState(userId));
        if (!userText.equals("/start") || tmpStorage.getState(userId).ordinal() <= botState.ordinal()) { // кейс, чтобы в случае ввода /start во время/после реги, пользака не отбросило назад
            tmpStorage.setState(userId, botState);
        }
        return handlingResolver.resolveHandler(tmpStorage.getState(userId), update);
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
