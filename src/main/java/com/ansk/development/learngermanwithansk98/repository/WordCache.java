package com.ansk.development.learngermanwithansk98.repository;

import com.ansk.development.learngermanwithansk98.service.model.input.Word;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Cache to store {@link Word}s in the service of {@link AddNewWord}.
 *
 * @author Anton Skripin
 */
@Component
public class WordCache {
  List<Word> words = new ArrayList<>();

  /**
   * Adds a word into the cache.
   *
   * @param word {@link Word}
   */
  public void addWord(Word word) {
    words.add(word);
  }

  /**
   * Gets all cached words.
   *
   * @return cached words
   */
  public List<Word> getWords() {
    return Collections.unmodifiableList(words);
  }

  /**
   * Deletes a cached word.
   *
   * @param word word to be removed
   */
  public void deleteWord(Word word) {
    words.remove(word);
  }

  /** Cleans the cached words. */
  public void cleanCache() {
    words.clear();
  }
}
