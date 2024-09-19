package com.ansk.development.learngermanwithansk98.repository;

import com.ansk.development.learngermanwithansk98.service.model.output.ExerciseDocument;
import org.springframework.stereotype.Component;

/**
 * Cache to store corrected text metadata.
 *
 * @author Anton Skripin
 */
@Component
public class CorrectedTextCache {

    private CorrectedTexContainer correctedTextContainer;

    /**
     * Returns currently cached {@link CorrectedTexContainer}.
     *
     * @return {@link CorrectedTexContainer}
     */
    public CorrectedTexContainer getCorrectedText() {
        return correctedTextContainer;
    }

    /**
     * Sets the {@link CorrectedTexContainer} into the cache.
     *
     * @param correctedTextMetadata {@link CorrectedTexContainer}
     */
    public void saveCorrectedText(CorrectedTexContainer correctedTextMetadata) {
        this.correctedTextContainer = correctedTextMetadata;
    }

    public void clear() {
        correctedTextContainer = null;
    }

    public record CorrectedTexContainer(ExerciseDocument originalText, ExerciseDocument correctedText) {
    }
}