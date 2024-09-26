package com.ansk.development.learngermanwithansk98.service.impl;

import com.ansk.development.learngermanwithansk98.service.model.Navigation;
import com.ansk.development.learngermanwithansk98.service.model.input.Word;
import com.ansk.development.learngermanwithansk98.service.model.output.WordInfo;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.Temporal;
import java.util.*;
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

    /**
     * Maps an update message to navigation parameters.
     *
     * @param update telegram message
     * @return navigation
     */
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

    /**
     * Maps the collection of {@link Word} to the projection collection of {@link WordInfo}.
     *
     * @param words words
     * @return projection collections
     */
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

    /**
     * Maps a {@link Temporal} object into its pretty printed version for the German locale.
     *
     * @param time temporal time
     * @return pretty-printed time
     */
    public static String mapToDateGermanFormat(Temporal time) {
        DateTimeFormatter germanFormatter = DateTimeFormatter.ofPattern("d. MMMM yyyy", Locale.GERMAN);
        LocalDate date = LocalDate.of(time.get(ChronoField.YEAR), time.get(ChronoField.MONTH_OF_YEAR), time.get(ChronoField.DAY_OF_MONTH));
        return date.format(germanFormatter);
    }
}
