package com.ansk.development.learngermanwithansk98.service.model;

import com.ansk.development.learngermanwithansk98.service.model.input.Word;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WordCard {
    private String id;
    private String title;
    private List<Word> words = new ArrayList<Word>();

    protected WordCard() {
    }

    public WordCard(String title, List<Word> words) {
        this.id = RandomStringUtils.randomAlphabetic(10);
        this.title = title;
        this.words = words;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public List<Word> getWords() {
        return Collections.unmodifiableList(words);
    }
}
