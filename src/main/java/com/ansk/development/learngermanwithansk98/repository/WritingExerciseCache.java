package com.ansk.development.learngermanwithansk98.repository;

import com.ansk.development.learngermanwithansk98.service.model.output.WritingExercise;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Cache to store a {@link WritingExercise}.
 *
 * @author Anton Skripin
 */
@Component
public class WritingExerciseCache {
    private WritingExercise writingExercise;

    /**
     * Saves a writing exercise into the cache.
     *
     * @param writingExercise writing exercise
     */
    public void saveWritingExercise(WritingExercise writingExercise) {
        this.writingExercise = writingExercise;
    }

    /**
     * Clears the cache
     */
    public void clearCache() {
        this.writingExercise = null;
    }

    /**
     * Returns the cached writing exercise or empty if nothing is cached.
     *
     * @return cached writing exercise or empty optional
     */
    public Optional<WritingExercise> cachedWritingExercise() {
        return Optional.ofNullable(this.writingExercise);
    }
}
