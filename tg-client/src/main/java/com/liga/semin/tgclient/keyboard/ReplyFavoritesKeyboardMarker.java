package com.liga.semin.tgclient.keyboard;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import static com.liga.semin.tgclient.keyboard.KeyboardBuilder.buildNRowKeyboard;

@Component
public class ReplyFavoritesKeyboardMarker {
    public ReplyKeyboard getFavoritesMenuKeyboard() {
        var row1 = new KeyboardRow();
        var row2 = new KeyboardRow();
        var row3 = new KeyboardRow();

        row1.add(new KeyboardButton(ButtonName.FOLLOWERS.getName()));
        row1.add(new KeyboardButton(ButtonName.FAVORITES.getName()));
        row2.add(new KeyboardButton(ButtonName.MUTUAL.getName()));
        row3.add(new KeyboardButton(ButtonName.GO_TO_MAIN_MENU.getName()));
        return buildNRowKeyboard(row1, row2, row3);
    }

    public ReplyKeyboard getIteratingFavoritesKeyboard() {
        var row1 = new KeyboardRow();
        var row2 = new KeyboardRow();

        row1.add(new KeyboardButton(ButtonName.BACKWARD.getName()));
        row1.add(new KeyboardButton(ButtonName.FORWARD.getName()));
        row2.add(new KeyboardButton(ButtonName.GO_TO_MAIN_MENU.getName()));
        return buildNRowKeyboard(row1, row2);
    }
}
