package com.liga.semin.tgclient.bot_api.message_handlers;

import com.liga.semin.tgclient.bot_api.BotState;
import com.liga.semin.tgclient.keyboard.ReplyMainMenuKeyboardMarker;
import com.liga.semin.tgclient.util.UpdateProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
public class MainMenuHandlerImpl implements MessageHandler {
    private final BotState handlerState = BotState.MAIN_MENU;
    private final ReplyMainMenuKeyboardMarker replyMainMenuKeyboard;

    @Override
    public BotApiMethod<?> handleUpdate(Update update) {
        SendMessage reply = new SendMessage(UpdateProcessor.getChatId(update), "Вы в главном меню");
        reply.setReplyMarkup(replyMainMenuKeyboard.getMainMenuKeyboard());
        return reply;
    }

    @Override
    public BotState getHanlderState() {
        return handlerState;
    }
}
