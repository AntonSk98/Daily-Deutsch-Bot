package com.ansk.development.learngermanwithansk98.service.model.output;

import java.util.List;

/**
 * Encapsulates objects and all necessary parameters related to a reading exercise part.
 *
 * @author Anton Skripin
 */
public record ReadingExercise(String title, Paragraphs paragraphs, ReadingTasks tasks, Images document) {
    public record TextOutput(String level, String title, String text) {
    }

    public record ReadingTasks(List<Task> tasks) {
    }

    public record Task(String question, String answer) {
    }

    public record FormattedText(String text) {
    }

    public record Paragraphs(List<String> paragraphs) {
    }

    public record Document(String level, String title, List<String> paragraphs, List<String> questions) {}
}
