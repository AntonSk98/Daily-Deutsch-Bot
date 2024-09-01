package com.ansk.development.learngermanwithansk98.service.impl;

import com.ansk.development.learngermanwithansk98.service.model.Navigation;
import com.ansk.development.learngermanwithansk98.service.model.input.Word;
import com.ansk.development.learngermanwithansk98.service.model.output.WordInfo;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ansk.development.learngermanwithansk98.service.model.Navigation.NEXT;
import static com.ansk.development.learngermanwithansk98.service.model.Navigation.PREVIOUS;

/**
 * Mapping utility class.
 *
 * @author Anton Skripin
 */
public class MapperUtils {

    public static Navigation map(Update update) {
        return Optional.ofNullable(update.getCallbackQuery())
                .map(CallbackQuery::getData)
                .map(data -> {
                    if (data.equals(PREVIOUS.getCommand())) {
                        return Navigation.previous();
                    }
                    if (data.equals(NEXT.getCommand())) {
                        return Navigation.next();
                    }

                    return null;
                }).orElse(null);
    }

    public static Collection<WordInfo> map(Collection<Word> words) {
        Map<String, Long> wordToTotalOccurrences = words
                .stream()
                .map(Word::getWord)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        Map<String, Long> wordToCurrentIndex = new HashMap<>();

        return words
                .stream()
                .map(word -> {
                    String wordStr = word.getWord();
                    long currentIndex = wordToCurrentIndex.getOrDefault(wordStr, 0L) + 1;
                    wordToCurrentIndex.put(wordStr, currentIndex);

                    long totalOccurrences = wordToTotalOccurrences.getOrDefault(wordStr, 0L);
                    return totalOccurrences > 1
                            ? WordInfo.of(currentIndex, wordStr, word.getTranslation())
                            : WordInfo.of(wordStr, word.getTranslation());
                })
                .toList();
    }
}
