package com.ansk.development.learngermanwithansk98.service.model.output;

import com.ansk.development.learngermanwithansk98.service.model.input.Word;

import java.util.Objects;
import java.util.Optional;

/**
 * Projection of a {@link Word} with a pretty-print support.
 *
 * @author Anton Skripin
 */
public record WordInfo(Long index, String word, String translation) {
    private static final String SEPARATOR = "#";
    private static final String DASH = "-";

    /**
     * Creates a {@code WordInfo} instance with a word and its translation, without an index.
     *
     * @param word        the word to be stored
     * @param translation the translation of the word
     * @return a new {@code WordInfo} instance without an index
     */
    public static WordInfo of(String word, String translation) {
        return new WordInfo(null, word, translation);
    }

    /**
     * Creates a {@code WordInfo} instance with an index, word, and its translation.
     *
     * @param index       the index of the word in some ordered collection
     * @param word        the word to be stored
     * @param translation the translation of the word
     * @return a new {@code WordInfo} instance with the specified index, word, and translation
     */
    public static WordInfo of(Long index, String word, String translation) {
        return new WordInfo(index, word, translation);
    }

    /**
     * Provides a formatted string representation of the word and its translation.
     * If the word has an index, it is included in the output. If the translation is null,
     * a question mark is used as a placeholder.
     *
     * @return a pretty-printed string containing the word, index (if present), and translation
     */
    public String prettyPrint() {
        String root = word + " " + DASH + " " + Optional.ofNullable(translation).orElse("?");
        if (Objects.nonNull(index)) {
            String prefix = index + SEPARATOR;
            return prefix + root;
        }
        return root;
    }

    /**
     * Finds a {@code WordInfo} by its reference, which is constructed using the word's index
     * and word itself. If the reference matches the constructed reference, the word is returned.
     *
     * @param reference the reference string, expected to be in the format "index#word" or "word" (if no index)
     * @return an {@link Optional} containing this {@code WordInfo} if the reference matches, otherwise empty
     */
    public Optional<WordInfo> findByReference(String reference) {
        String expectedReference = (Objects.isNull(index) ? "" : index + SEPARATOR) + word;
        if (reference.equals(expectedReference)) {
            return Optional.of(this);
        }
        return Optional.empty();
    }
}
