package com.ansk.development.learngermanwithansk98.service.impl;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.gateway.OutputGateway;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.service.api.ICommandService;
import com.ansk.development.learngermanwithansk98.service.model.Command;
import com.ansk.development.learngermanwithansk98.service.model.CommandParameters;
import com.ansk.development.learngermanwithansk98.service.model.CommandState;
import com.ansk.development.learngermanwithansk98.service.model.AbstractCommandModel;
import org.apache.commons.lang3.StringUtils;

/**
 * Abstract implementation of {@link ICommandService}.
 * This is required to handle parameters of a {@link Command} if provided by {@link CommandsConfiguration.CommandDefinition}.
 *
 * @author Anton Skripin
 */
public abstract class AbstractCommandService implements ICommandService {

    private final CommandsConfiguration commandsConfiguration;
    private final OutputGateway outputGateway;
    private final CommandCache commandCache;

    protected AbstractCommandService(CommandsConfiguration commandsConfiguration,
                                     OutputGateway outputGateway,
                                     CommandCache commandCache) {
        this.commandsConfiguration = commandsConfiguration;
        this.outputGateway = outputGateway;
        this.commandCache = commandCache;
    }

    @Override
    public void execute(CommandParameters commandParameters) {
        Command command = supportedCommand();
        AbstractCommandModel<?> model = supportedCommandModel();
        CommandState commandState = commandCache.getOrInit(command, model);
        if (StringUtils.isNotEmpty(commandState.getAwaitingKey())) {
            commandState.getCurrentCommandModel().append(commandState.getAwaitingKey(), commandParameters.input());
        }

        if (commandState.getCurrentCommandModel().getParamIterator().hasNext()) {
            String key = commandState.getCurrentCommandModel().getParamIterator().next();
            String prompt = commandsConfiguration.findPrompt(command.getPath(), key);
            commandState.setAwaitingKey(key);
            System.out.println("Test is reference updated?");
            outputGateway.sendPlainMessage(commandParameters.chatId(), prompt);
        } else {
            finishExecute(commandParameters);
            commandCache.clear(command);
        }
    }

}
