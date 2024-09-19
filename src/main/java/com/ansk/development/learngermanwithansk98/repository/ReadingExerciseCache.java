package com.ansk.development.learngermanwithansk98.repository;


import com.ansk.development.learngermanwithansk98.service.model.output.ReadingExercise;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Cache to store a {@link ReadingExercise}.
 *
 * @author Anton Skripin
 */
@Component
public class ReadingExerciseCache {
    private ReadingExercise readingExercise;

    /**
     * Saves a reading exercise into cache.
     *
     * @param readingExercise reading exercise
     */
    public void saveReadingExercise(ReadingExercise readingExercise) {
        this.readingExercise = readingExercise;
    }

    /**
     * Clears the cache.
     */
    public void clearCache() {
        this.readingExercise = null;
    }

    /**
     * Returns the cached reading exercise or empty if nothing is cached.
     *
     * @return cached reading exercise or empty optional
     */
    public Optional<ReadingExercise> cachedReadingExercise() {
        return Optional.ofNullable(this.readingExercise);
    }
}
