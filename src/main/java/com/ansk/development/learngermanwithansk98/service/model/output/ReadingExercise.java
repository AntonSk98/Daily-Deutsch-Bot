package com.ansk.development.learngermanwithansk98.service.model.output;

import java.util.List;

/**
 * Encapsulates objects and all necessary parameters for a reading exercise.
 *
 * @param title text title
 * @param textParts  text
 *
 * @author Anton Skripin
 */
public record ReadingExercise(String level, String title, List<String> textParts, List<Question> questions) {

    public record Question(String question) {
    }
}
