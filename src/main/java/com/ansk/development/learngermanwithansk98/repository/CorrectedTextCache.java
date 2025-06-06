package com.ansk.development.learngermanwithansk98.repository;

import com.ansk.development.learngermanwithansk98.service.model.output.ExerciseDocument;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Cache to store corrected text alongside the original text.
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
    public Optional<CorrectedTexContainer> getCorrectedText() {
        return Optional.ofNullable(correctedTextContainer);
    }

    /**
     * Sets the {@link CorrectedTexContainer} into the cache.
     *
     * @param correctedTextMetadata {@link CorrectedTexContainer}
     */
    public void saveCorrectedText(CorrectedTexContainer correctedTextMetadata) {
        this.correctedTextContainer = correctedTextMetadata;
    }

    /**
     * Clears the cache.
     */
    public void clear() {
        correctedTextContainer = null;
    }

    /**
     * Container to store the state of the exercise with a corrected text.
     *
     * @param correctedText               corrected text
     * @param nonCorrectedTextDocument    original text with mistakes
     * @param textWithCorrectionsDocument text with highlighted mistakes and highlighted corrections
     */
    public record CorrectedTexContainer(String correctedText,
                                        ExerciseDocument nonCorrectedTextDocument,
                                        ExerciseDocument textWithCorrectionsDocument) {
    }
}