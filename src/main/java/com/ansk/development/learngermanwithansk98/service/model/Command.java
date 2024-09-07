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
    PREVIEW("/preview_word_card"),

    READING_EXERCISE_GENERATE("/generate_reading_exercise"),
    READING_EXERCISE_CREATE("/create_reading_exercise"),
    READING_EXERCISE_PREVIEW("/cached_reading_exercise"),

    WRITING_WITH_EXAMPLE("/writing_with_example"),

    LISTENING_EXERCISE("/listening_exercise");

    private final String path;

    Command(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public static Optional<Command> find(String command) {
        return Arrays.stream(Command.values())
                .filter(cmd -> cmd.getPath().equals(command))
                .findFirst();
    }
}
