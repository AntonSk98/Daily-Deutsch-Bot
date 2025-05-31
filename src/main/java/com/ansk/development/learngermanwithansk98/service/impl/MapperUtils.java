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
     * Maps a collection of {@link Word} objects to a map where the key is a corresponding {@link WordInfo}
     * and the value is the original {@link Word}. When the same word appears multiple times,
     * each occurrence is indexed in the {@link WordInfo}.
     *
     * @param words the collection of {@link Word} to map
     * @return a map from {@link WordInfo} to {@link Word}, preserving insertion order
     */
    public static Map<WordInfo, Word> map(Collection<Word> words) {
        Map<String, Long> totalOccurrences = words.stream()
                .collect(Collectors.groupingBy(Word::getWord, Collectors.counting()));

        Map<String, Long> currentIndex = new HashMap<>();
        Map<WordInfo, Word> result = new LinkedHashMap<>();

        for (Word word : words) {
            String wordString = word.getWord();
            long index = currentIndex.merge(wordString, 1L, Long::sum);
            long total = totalOccurrences.get(wordString);

            WordInfo info = total > 1
                    ? WordInfo.of(index, wordString, word.getTranslation())
                    : WordInfo.of(wordString, word.getTranslation());

            result.put(info, word);
        }

        return result;
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
