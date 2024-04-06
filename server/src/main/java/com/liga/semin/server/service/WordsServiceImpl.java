package com.liga.semin.server.service;

import com.liga.semin.server.model.PhitaWord;
import com.liga.semin.server.model.YatWord;
import com.liga.semin.server.repository.PhitaWordRepository;
import com.liga.semin.server.repository.YatWordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WordsServiceImpl implements WordsService {
    private final PhitaWordRepository phitaWordRepository;
    private final YatWordRepository yatWordRepository;

    public Set<String> getPhitaWordsSet() {
        return phitaWordRepository.findAll().stream()
                .map(PhitaWord::getWord)
                .collect(Collectors.toSet());
    }

    public Set<String> getYatWordsSet() {
        return yatWordRepository.findAll().stream()
                .map(YatWord::getWord)
                .collect(Collectors.toSet());
    }

}
