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

    public void saveReadingExercise(ReadingExercise readingExercise) {
        this.readingExercise = readingExercise;
    }

    public void clearCache() {
        this.readingExercise = null;
    }

    public Optional<ReadingExercise> cachedReadingExercise() {
        return Optional.ofNullable(this.readingExercise);
    }
}
