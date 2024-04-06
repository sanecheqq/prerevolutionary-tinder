package com.liga.semin.server.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class TranslatorServiceImpl implements TranslatorService {
    private final WordsService wordsService;
    private final Set<Character> vowels = Set.of('а', 'о', 'у', 'ы', 'э', 'е', 'ё', 'и', 'ю', 'я', 'й');
    private static final Logger logger = LoggerFactory.getLogger(TranslatorServiceImpl.class);

    @Override
    public String translateTextToOldRussian(String text) {
        logger.debug("Starting translation of text {}", text);
        text = translateHardSign(text);
        text = translateDecimalI(text);
        text = translateOldLetter(text, 'ф', 'ѳ', wordsService.getPhitaWordsSet());
        text = translateOldLetter(text, 'е', 'ѣ', wordsService.getPhitaWordsSet());
        logger.debug("Translation result: {}", text);
        return text;
    }

    private String translateOldLetter(String text, char from, char to, Set<String> data) {
        logger.debug("Translating from modern letter {} to old {}", from, to);

        var sb = new StringBuilder();
        for (var word : text.split("\\s")) {
            var sbWord = new StringBuilder(word);
            for (int i = 0; i < word.length(); i++) {
                char cur = sbWord.charAt(i);
                char fromTmp = convertToTheSameCase(cur, from);
                char toTmp = convertToTheSameCase(cur, to);
                if (cur == fromTmp)
                    sbWord.setCharAt(i, toTmp);
                if (data.contains(sbWord.toString())) { // пробуем заменить букву и проверяем, есть ли такое слово
                    break;
                }
                sbWord.setCharAt(i, cur); // если такого слова нет, ставим букву, которая была
            }
            sb.append(sbWord) // добавляем либо измененную версию (в случае break), либо старую, если заменить не удалось
                    .append(" ");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    private Character convertToTheSameCase(Character source, Character target) {
        if (Character.isUpperCase(source))
            return Character.toUpperCase(target);
        else return Character.toLowerCase(target);
    }

    private String translateDecimalI(String text) {
        logger.debug("Translating decimal i");
        var sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char cur = text.charAt(i);
            char curLC = Character.toLowerCase(cur);
            if (i + 1 < text.length() && curLC == 'и' && vowels.contains(Character.toLowerCase(text.charAt(i + 1)))) // i ставится вместо и, если далее согласная
                sb.append('i');
            else
                sb.append(cur);
        }
        return sb.toString();
    }

    private String translateHardSign(String text) {
        logger.debug("Translating hard sign");

        var sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char cur = text.charAt(i);
            char curLC = Character.toLowerCase(cur);
            char hardSign = convertToTheSameCase(cur, 'ъ');
            if (curLC == 'ь') { // кейс, когда нам нужно аппендить не текущую букву или символ, а твердый знак вместо нее
                sb.append(hardSign);
                continue;
            }
            sb.append(cur);
            if (!Character.isLetter(cur))
                continue;
            if (i+1 < text.length() && !vowels.contains(curLC) && !Character.isLetter(text.charAt(i+1)))
                sb.append(hardSign);
            else if (i == text.length() - 1 && !vowels.contains(curLC))
                sb.append(hardSign);
        }
        return sb.toString();
    }


}
