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

    /**
     * Saves a listening exercise into cache.
     *
     * @param listeningExercise listening exercise
     */
    public void saveListeningExercise(ListeningExercise listeningExercise) {
        this.listeningExercise = listeningExercise;
    }

    /**
     * Clears the cache.
     */
    public void clearCache() {
        this.listeningExercise = null;
    }

    /**
     * Returns cached listening exercise or empty if nothing is cached.
     *
     * @return cached listening exercise or empty optional
     */
    public Optional<ListeningExercise> cachedListeningExercise() {
        return Optional.ofNullable(this.listeningExercise);
    }
}
