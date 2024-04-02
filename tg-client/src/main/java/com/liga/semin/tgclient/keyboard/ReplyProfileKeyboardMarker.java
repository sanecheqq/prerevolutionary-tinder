package com.liga.semin.tgclient.keyboard;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import static com.liga.semin.tgclient.keyboard.KeyboardBuilder.buildNRowKeyboard;


@Component
public class ReplyProfileKeyboardMarker {
    public ReplyKeyboardMarkup getProfileKeyboard() {
        var row1 = new KeyboardRow();

        row1.add(new KeyboardButton(ButtonName.RESET.getName()));
        row1.add(new KeyboardButton(ButtonName.GO_TO_MAIN_MENU.getName()));

        return buildNRowKeyboard(row1);
    }
}
