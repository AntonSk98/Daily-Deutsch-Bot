package com.ansk.development.learngermanwithansk98.service.api;

import com.ansk.development.learngermanwithansk98.service.model.Command;
import com.ansk.development.learngermanwithansk98.service.model.CommandParameters;
import com.ansk.development.learngermanwithansk98.service.model.AbstractCommandModel;

/**
 * Common entry point to navigate and execute {@link Command}.
 *
 * @author Anton Skripin
 */
public interface ICommandService {
    Command supportedCommand();

    void execute(CommandParameters commandParameters);

    void finishExecute(CommandParameters commandParameters);

    AbstractCommandModel<?> supportedCommandModel();
}
