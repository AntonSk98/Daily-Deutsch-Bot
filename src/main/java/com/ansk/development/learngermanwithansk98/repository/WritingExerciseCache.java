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

    public void saveWritingExercise(WritingExercise writingExercise) {
        this.writingExercise = writingExercise;
    }

    public void clearCache() {
        this.writingExercise = null;
    }

    public Optional<WritingExercise> cachedWritingExercise() {
        return Optional.ofNullable(this.writingExercise);
    }
}
