package com.ansk.development.learngermanwithansk98.service.model.output;

import com.ansk.development.learngermanwithansk98.service.model.input.Word;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a word card, which contains a list of words and a title.
 * Each word card has a unique identifier generated automatically.
 *
 * @author Anton Skripin
 */
public class WordCard {
    private String id;
    private String title;
    private List<Word> words = new ArrayList<Word>();

    protected WordCard() {
    }

    /**
     * Creates a new {@code WordCard} with the specified title and list of words.
     * The card is assigned a unique identifier.
     *
     * @param title the title of the word card
     * @param words the list of {@link Word} objects to associate with the word card
     */
    public WordCard(String title, List<Word> words) {
        this.id = RandomStringUtils.randomAlphabetic(10);
        this.title = title;
        this.words = words;
    }

    /**
     * Retrieves the unique identifier of the word card.
     *
     * @return the unique ID of the word card as a {@link String}
     */
    public String getId() {
        return id;
    }

    /**
     * Retrieves the title of the word card.
     *
     * @return the title of the word card as a {@link String}
     */
    public String getTitle() {
        return title;
    }

    /**
     * Retrieves an unmodifiable list of words associated with the word card.
     *
     * @return an unmodifiable {@link List} of {@link Word} objects
     */
    public List<Word> getWords() {
        return Collections.unmodifiableList(words);
    }
}
