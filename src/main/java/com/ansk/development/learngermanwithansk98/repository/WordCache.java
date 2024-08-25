package com.ansk.development.learngermanwithansk98.repository;

import com.ansk.development.learngermanwithansk98.service.impl.AddNewWordCommandService;
import com.ansk.development.learngermanwithansk98.service.model.Word;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Cache to store {@link Word}s in the service of {@link AddNewWordCommandService}.
 *
 * @author Anton Skripin
 */
@Component
public class WordCache {
    List<Word> words = new ArrayList<>();

    public void addWord(Word word) {
        words.add(word);
    }

    public List<Word> getWords() {
        return words;
    }

    public void deleteWord(Word word) {
        words.remove(word);
    }

    public void cleanCache() {
        words.clear();
    }
}
