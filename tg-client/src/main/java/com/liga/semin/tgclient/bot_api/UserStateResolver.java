package com.liga.semin.tgclient.bot_api;

import com.liga.semin.tgclient.temporary_storage.TemporaryUserStateStorage;
import com.liga.semin.tgclient.util.UpdateProcessor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Map;

/**
 * Внешний класс-resolver для постановки определяющего состояния бота для дальнейших действий.
 * Поставленное состояние будет влиять на то, какой handler будет выбран для обработки ответа пользователя
 * В данном классе определены только поверхностные состояния-сценарии. Более подробными состояниями оперируют handler-ы
 */
@Component
@RequiredArgsConstructor
public class UserStateResolver {
    private final TemporaryUserStateStorage tmpStorage;
    private final MessageHandlingResolver handlingResolver;
    private Map<String, BotState> userStates;
    private static final Logger logger = LoggerFactory.getLogger(UserStateResolver.class);

    {
        userStates = Map.of(
                "/start", BotState.INIT,
                "Зарегистрироваться", BotState.SET_GENDER,
                "Анкета", BotState.MY_PROFILE,
                "Любимцы", BotState.FAVORITES_MENU,
                "Поиск", BotState.SEARCHING,
                "Перейти в главное меню", BotState.MAIN_MENU,
                "Сброс данных профиля", BotState.RESET
        );
    }

    public List<PartialBotApiMethod<?>> resolveUser(Update update) {
        var userId = UpdateProcessor.getUserId(update);
        var answer = UpdateProcessor.getAnswer(update);
        logger.debug("Resolving user {} state", userId);

        BotState botState = userStates.getOrDefault(answer, tmpStorage.getState(userId));
        if (!answer.equals("/start") || tmpStorage.getState(userId).ordinal() <= botState.ordinal()) { // кейс, чтобы в случае ввода /start во время/после реги, пользака не отбросило назад
            tmpStorage.setState(userId, botState);
        }
        logger.debug("User {} cur state: {}", userId, tmpStorage.getState(userId));
        return handlingResolver.resolveHandler(tmpStorage.getState(userId), update);
    }
}
