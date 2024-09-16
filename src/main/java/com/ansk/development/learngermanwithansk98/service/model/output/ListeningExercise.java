package com.ansk.development.learngermanwithansk98.service.model.output;

import java.util.List;

/**
 * Represents the details of a listening exercise, including audio, tasks, and an associated document.
 * This record encapsulates the main components of a listening exercise.
 *
 * @param audio    the audio file or ID for the listening exercise
 * @param level    difficulty of a listening exercise
 * @param tasks    the {@link ListeningTasks} associated with the listening exercise
 * @param document the {@link ExerciseDocument} containing additional document details for the exercise
 * @author Anton Skripin
 */
public record ListeningExercise(String audio, String level, ListeningTasks tasks, ExerciseDocument document) {

    /**
     * Represents the structure for a listening exercise, including the level, title, and a list of tasks.
     *
     * @param level the difficulty level of the exercise
     * @param title the title of the listening exercise
     * @param tasks the list of {@link Task} objects that make up the exercise
     */
    public record Output(String level, String title, List<Task> tasks) {
    }

    /**
     * Represents a single task within a listening exercise, containing a question and its corresponding answer.
     *
     * @param question the question asked in the task
     * @param answer   the expected answer to the question
     */
    public record Task(String question, String answer) {
    }

    /**
     * Represents a list of paragraphs related to the transcribed audio
     *
     * @param paragraphs a list of {@link String} paragraphs associated with the exercise
     */
    public record Paragraphs(List<String> paragraphs) {
    }

    /**
     * Encapsulates the listening tasks for a given exercise, containing a list of individual {@link Task} objects.
     *
     * @param tasks the list of tasks within the listening exercise
     */
    public record ListeningTasks(List<Task> tasks) {
    }

    /**
     * Represents a document related to the listening exercise, including metadata like level, title, and content such as paragraphs and questions.
     *
     * @param level      the difficulty level of the document
     * @param title      the title of the document
     * @param paragraphs the list of paragraphs contained in the document
     * @param questions  the list of questions related to the document content
     */
    public record Document(String level, String title, List<String> paragraphs, List<String> questions) {
    }
}
