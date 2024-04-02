package com.liga.semin.tgclient.keyboard;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;

@Component
public class ReplyRegistrationKeyboardMarker {
    public ReplyKeyboardMarkup getRegistrationKeyboard() {
        var row1 = new KeyboardRow();
        row1.add(new KeyboardButton(ButtonName.REGISTER.getName()));
        return buildOneRowKeyboard(row1);
    }

    public ReplyKeyboard getGenderKeyboard() {
        var row1 = new KeyboardRow();
        row1.add(new KeyboardButton(ButtonName.MALE.getName()));
        row1.add(new KeyboardButton(ButtonName.FEMALE.getName()));
        return buildOneRowKeyboard(row1);
    }

    public ReplyKeyboard getGoToMainMenuKeyboard() {
        var row1 = new KeyboardRow();
        row1.add(new KeyboardButton(ButtonName.GO_TO_MAIN_MENU.getName()));
        return buildOneRowKeyboard(row1);
    }

    private static ReplyKeyboardMarkup buildOneRowKeyboard(KeyboardRow row1) {
        List<KeyboardRow> keyboard = List.of(row1);

        final var replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboard);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        return replyKeyboardMarkup;
    }

    public ReplyKeyboard removeReplyKeyboard() {
        var replyKeyboardRemove = new ReplyKeyboardRemove();
        replyKeyboardRemove.setRemoveKeyboard(true);
        return replyKeyboardRemove;
    }


}
