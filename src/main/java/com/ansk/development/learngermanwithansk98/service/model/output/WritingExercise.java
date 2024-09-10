package com.ansk.development.learngermanwithansk98.service.model.output;

import java.util.List;

/**
 * Represents a generated writing exercise, including its topic and associated document with writing sample.
 *
 * @param topic            the topic of the writing exercise
 * @param exerciseDocument the {@link ExerciseDocument} containing details and content for the writing exercise
 * @author Anton Skripin
 */
public record WritingExercise(String topic, ExerciseDocument exerciseDocument) {

    /**
     * Represents the structure of a writing exercise, including its level, topic, and a list of writing tasks.
     *
     * @param level   the difficulty level of the writing exercise
     * @param topic   the topic of the writing exercise
     * @param writing the list of writing tasks or content related to the exercise
     */
    public record Output(String level, String topic, List<String> writing) {
    }
}
