package com.ansk.development.learngermanwithansk98.repository;

import com.ansk.development.learngermanwithansk98.service.model.output.ListeningExercise;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Cache to store a {@link ListeningExercise}.
 *
 * @author Anton Skripin
 */
@Component
public class ListeningExerciseCache {
    private ListeningExercise listeningExercise;

    public void saveListeningExercise(ListeningExercise listeningExercise) {
        this.listeningExercise = listeningExercise;
    }

    public void clearCache() {
        this.listeningExercise = null;
    }

    public Optional<ListeningExercise> cachedListeningExercise() {
        return Optional.ofNullable(this.listeningExercise);
    }
}
