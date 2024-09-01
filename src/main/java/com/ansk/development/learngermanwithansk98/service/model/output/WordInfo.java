package com.ansk.development.learngermanwithansk98.service.model;

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

    public static WordInfo of(String word, String translation) {
        return new WordInfo(null, word, translation);
    }

    public static WordInfo of(Long index, String word, String translation) {
        return new WordInfo(index, word, translation);
    }

    public String prettyPrint() {
        String root = word + " " + DASH + " " + Optional.ofNullable(translation).orElse("?");
        if (Objects.nonNull(index)) {
            String prefix = index + SEPARATOR;
            return prefix + root;
        }
        return root;
    }

    public Optional<WordInfo> findByReference(String reference) {
        String expectedReference = (Objects.isNull(index) ? "" : index + SEPARATOR) + word;
        if (reference.equals(expectedReference)) {
            return Optional.of(this);
        }
        return Optional.empty();
    }
}
