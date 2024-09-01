package com.ansk.development.learngermanwithansk98.repository;

import com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel;
import com.ansk.development.learngermanwithansk98.service.model.Command;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


/**
 * Cache that manages the state and processing of {@link Command}.
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


    public CommandState getOrInit(Command command, AbstractCommandModel<?> model) {
        if (!commandToModel.containsKey(command)) {
            commandToModel.put(command, new CommandState(model));
        }

        return commandToModel.get(command);
    }

    public void clear() {
        currentCommand = null;
        commandToModel.clear();
    }

    public Command getCurrentCommand() {
        return currentCommand;
    }

    public void setCurrentCommand(Command currentCommand) {
        this.currentCommand = currentCommand;
    }
}
