package com.liga.semin.tgclient.keyboard;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import static com.liga.semin.tgclient.keyboard.KeyboardBuilder.buildNRowKeyboard;

@Component
public class ReplySearchingKeyboardMarker {
    public ReplyKeyboard getSearchingKeyboard() {
        var row1 = new KeyboardRow();
        var row2 = new KeyboardRow();

        row1.add(new KeyboardButton(ButtonName.LEFT.getName()));
        row1.add(new KeyboardButton(ButtonName.RIGHT.getName()));
        row2.add(new KeyboardButton(ButtonName.GO_TO_MAIN_MENU.getName()));
        return buildNRowKeyboard(row1, row2);
    }
}
