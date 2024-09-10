package com.ansk.development.learngermanwithansk98.service.model.output;

import java.util.List;

/**
 * Encapsulates the details and parameters related to a reading exercise part.
 *
 * @param title      the title of the reading exercise
 * @param paragraphs the {@link Paragraphs} associated with the reading exercise
 * @param tasks      the {@link ReadingTasks} containing the tasks for the exercise
 * @param document   the {@link ExerciseDocument} containing document of the reading exercise
 *
 * @author Anton Skripin
 */
public record ReadingExercise(String title, Paragraphs paragraphs, ReadingTasks tasks, ExerciseDocument document) {

    /**
     * Represents the textual output for the reading exercise, including level, title, and text.
     *
     * @param level the difficulty level of the reading exercise
     * @param title the title of the reading exercise
     * @param text  the full text content of the exercise
     */
    public record TextOutput(String level, String title, String text) {
    }

    /**
     * Represents the reading tasks for the exercise, containing a list of reading tasks.
     *
     * @param tasks the list of {@link Task} objects that make up the reading exercise tasks
     */
    public record ReadingTasks(List<Task> tasks) {
    }

    /**
     * Represents a single task within a reading exercise, consisting of a question and its corresponding answer.
     *
     * @param question the question asked in the reading exercise task
     * @param answer   the expected answer to the question
     */
    public record Task(String question, String answer) {
    }

    /**
     * Represents a collection of paragraphs of a text.
     *
     * @param paragraphs a list of {@link String} paragraphs associated with the reading exercise
     */
    public record Paragraphs(List<String> paragraphs) {
    }

    public record Document(String level, String title, List<String> paragraphs, List<String> questions) {
    }
}
