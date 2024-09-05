package com.ansk.development.learngermanwithansk98.service.model.output;

import java.util.List;

/**
 * Contains properties of a generated writing exercise.
 *
 * @param level   of the writing exercise
 * @param topic   of the writing exercise
 * @param writing the writing itself
 * @author Anton Skripin
 */
public record WritingExercise(String level, String topic, List<String> writing) {
}
