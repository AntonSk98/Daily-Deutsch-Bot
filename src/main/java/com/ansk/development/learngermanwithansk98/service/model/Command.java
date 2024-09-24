package com.ansk.development.learngermanwithansk98.service.model;

import java.util.Arrays;
import java.util.Optional;

/**
 * Defines all known commands.
 *
 * @author Anton Skripin
 */
public enum Command {
    ADD_NEW_WORD("/new_word"),
    AI_NEW_WORD("/ai_new_word"),
    GET_WORDS("/cached_words"),
    DELETE_WORD("/delete_word"),
    CLEAR_WORD_CACHE("/clear_word_card"),
    PREVIEW_WORD_CARD("/preview_word_card"),
    PUBLISH_WORD_CARD("/publish_word_card"),

    READING_EXERCISE_GENERATE("/generate_reading_exercise"),
    READING_EXERCISE_CREATE("/create_reading_exercise"),
    READING_EXERCISE_PREVIEW("/cached_reading_exercise"),

    WRITING_WITH_EXAMPLE("/writing_with_example"),
    WRITING_EXERCISE_PREVIEW("/preview_writing_exercise"),
    CORRECT_TEXT("/correct_text"),
    PUBLISH_WRITING_EXERCISE("/publish_writing_exercise"),

    LISTENING_EXERCISE("/listening_exercise"),
    LISTENING_EXERCISE_PREVIEW("/preview_listening_exercise"),
    LISTENING_EDIT_TRANSCRIPTION("/listening_edit_transcription"),
    PUBLISH_LISTENING_EXERCISE("/publish_listening_exercise");

    private final String path;

    /**
     * Creates a new command.
     *
     * @param path path
     */
    Command(String path) {
        this.path = path;
    }

    /**
     * Get path of current command
     *
     * @return pat
     */
    public String getPath() {
        return path;
    }

    /**
     * Finds a command by its string representation.
     *
     * @param command command as string
     * @return command
     */
    public static Optional<Command> find(String command) {
        return Arrays.stream(Command.values())
                .filter(cmd -> cmd.getPath().equals(command))
                .findFirst();
    }
}
