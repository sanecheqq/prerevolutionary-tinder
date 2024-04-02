package com.liga.semin.tgclient.keyboard;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import static com.liga.semin.tgclient.keyboard.KeyboardBuilder.*;
@Component
public class ReplyRegistrationKeyboardMarker {
    public ReplyKeyboardMarkup getRegistrationKeyboard() {
        var row1 = new KeyboardRow();
        row1.add(new KeyboardButton(ButtonName.REGISTER.getName()));
        return buildNRowKeyboard(row1);
    }

    public ReplyKeyboard getGenderKeyboard() {
        var row1 = new KeyboardRow();
        row1.add(new KeyboardButton(ButtonName.MALE.getName()));
        row1.add(new KeyboardButton(ButtonName.FEMALE.getName()));
        return buildNRowKeyboard(row1);
    }

    public ReplyKeyboard getGoToMainMenuKeyboard() {
        var row1 = new KeyboardRow();
        row1.add(new KeyboardButton(ButtonName.GO_TO_MAIN_MENU.getName()));
        return buildNRowKeyboard(row1);
    }

    public ReplyKeyboard getMateGenderKeyboard() {
        var row1 = new KeyboardRow();
        var row2 = new KeyboardRow();

        row1.add(new KeyboardButton(ButtonName.MALE.getName()));
        row1.add(new KeyboardButton(ButtonName.FEMALE.getName()));
        row2.add(new KeyboardButton(ButtonName.ALL.getName()));

        return buildNRowKeyboard(row1,row2);
    }

    public ReplyKeyboard removeReplyKeyboard() {
        var replyKeyboardRemove = new ReplyKeyboardRemove();
        replyKeyboardRemove.setRemoveKeyboard(true);
        return replyKeyboardRemove;
    }

}
