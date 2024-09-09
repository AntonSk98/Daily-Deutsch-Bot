package com.ansk.development.learngermanwithansk98.service.model.output;

import java.util.List;

/**
 * Contains properties of a generated writing exercise.
 *
 * @param topic   of the writing exercise
 * @param exerciseDocument writing exercise
 * @author Anton Skripin
 */
public record WritingExercise(String topic, ExerciseDocument exerciseDocument) {
    public record Output(String level, String topic, List<String> writing) {
    }

}
