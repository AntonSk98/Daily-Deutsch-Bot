package com.ansk.development.learngermanwithansk98.service.model.output;

import java.util.List;

/**
 * Contains information about listening exercise.
 *
 * @author Anton Skripin
 */
public record ListeningExercise(String audio, String level, String title, Paragraphs paragraphs, ListeningTasks tasks,
                                ExerciseDocument document) {
    public record Output(String level, String title, List<Task> tasks) {
    }

    public record Task(String question, String answer) {
    }

    public record Paragraphs(List<String> paragraphs) {
    }

    public record ListeningTasks(List<Task> tasks) {

    }
}
