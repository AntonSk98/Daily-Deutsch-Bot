package com.ansk.development.learngermanwithansk98.service.model;

import java.util.Arrays;
import java.util.Optional;

/**
 * Contains static handles classes to work with {@link Command}.
 *
 * @author Anton Skripin
 */
public class Commands {

    private Commands() {

    }

    public static Optional<Command> find(String command) {
        return Arrays.stream(Command.values())
                .filter(cmd -> cmd.getPath().equals(command))
                .findFirst();
    }
}
