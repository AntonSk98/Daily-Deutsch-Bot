package com.ansk.development.learngermanwithansk98.service.api;

import com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel;
import com.ansk.development.learngermanwithansk98.service.model.Command;
import com.ansk.development.learngermanwithansk98.service.model.input.CommandParameters;

/**
 * Common entry point to navigate and execute {@link Command}.
 *
 * @author Anton Skripin
 */
public interface ICommandService {
    Command supportedCommand();

    void processCommand(CommandParameters commandParameters);

    void applyCommandModel(AbstractCommandModel<?> currentCommandModel, CommandParameters commandParameters);

    AbstractCommandModel<?> supportedCommandModel();
}
