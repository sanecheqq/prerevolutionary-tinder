package com.liga.semin.tgclient.keyboard;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import static com.liga.semin.tgclient.keyboard.KeyboardBuilder.buildNRowKeyboard;

@Component
public class ReplyMainMenuKeyboardMarker {

    public ReplyKeyboardMarkup getMainMenuKeyboard() {
        var row1 = new KeyboardRow();
        var row2 = new KeyboardRow();

        row1.add(new KeyboardButton(ButtonName.SEARCHING.getName()));
        row1.add(new KeyboardButton(ButtonName.LIKES.getName()));
        row2.add(new KeyboardButton(ButtonName.PROFILE.getName()));

        return buildNRowKeyboard(row1, row2);
    }

}
