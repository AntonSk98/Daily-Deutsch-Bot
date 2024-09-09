package com.ansk.development.learngermanwithansk98.repository;

import com.ansk.development.learngermanwithansk98.service.model.Command;
import com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


/**
 * Cache that manages the state and processing of {@link Command}.
 * This class is responsible for storing and retrieving {@link CommandState}
 * objects based on a given {@link Command}. It also keeps track of the
 * currently active command.
 *
 * @author Anton Skripin
 */
@Component
public class CommandCache {

    private final Map<Command, CommandState> commandToModel;
    private Command currentCommand;

    private CommandCache() {
        this.commandToModel = new HashMap<>();
    }

    /**
     * Retrieves the {@link CommandState} for the given {@link Command}.
     * If the {@link CommandState} does not exist in the cache, it is initialized with the provided {@link AbstractCommandModel}.
     *
     * @param command the {@link Command} for which the state is to be retrieved or initialized
     * @param model   the {@link AbstractCommandModel} used to initialize the {@link CommandState}
     *                if it does not exist in the cache
     * @return the {@link CommandState} associated with the provided {@link Command}
     */
    public CommandState getOrInit(Command command, AbstractCommandModel<?> model) {
        if (!commandToModel.containsKey(command)) {
            commandToModel.put(command, new CommandState(model));
        }

        return commandToModel.get(command);
    }

    /**
     * Clears the cache by removing all stored command states and resetting
     * the current command to {@code null}.
     */
    public void clear() {
        currentCommand = null;
        commandToModel.clear();
    }

    /**
     * Retrieves the currently active {@link Command}.
     *
     * @return the current {@link Command}, or {@code null} if no command is active
     */
    public Command getCurrentCommand() {
        return currentCommand;
    }

    /**
     * Sets the currently active {@link Command}.
     *
     * @param currentCommand the {@link Command} to set as the current command
     */
    public void setCurrentCommand(Command currentCommand) {
        this.currentCommand = currentCommand;
    }
}
