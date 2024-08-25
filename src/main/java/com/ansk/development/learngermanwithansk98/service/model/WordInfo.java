package com.ansk.development.learngermanwithansk98.service.model;

import java.util.Objects;
import java.util.Optional;

/**
 * Projection of a {@link Word} with a pretty-print support.
 *
 * @author Anton Skripin
 */
public class WordInfo {
    private static final String SEPARATOR = "#";
    private static final String DASH = "-";

    private final Long index;
    private final String word;
    private final String translation;

    private WordInfo(Long index, String word, String translation) {
        this.index = index;
        this.word = word;
        this.translation = translation;
    }

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
        if (reference.equals((Objects.isNull(index) ? "" : index + SEPARATOR) + word)) {
            return Optional.of(this);
        }
        return Optional.empty();
    }

    public String word() {
        return word;
    }

    public String translation() {
        return translation;
    }
}
