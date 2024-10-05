package com.ansk.development.learngermanwithansk98.service.impl.command;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.integration.telegram.ITelegramClient;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.repository.CommandState;
import com.ansk.development.learngermanwithansk98.service.api.ICommandProcessor;
import com.ansk.development.learngermanwithansk98.service.model.Command;
import com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel;
import com.ansk.development.learngermanwithansk98.service.model.input.CommandParameters;

import java.util.ListIterator;

/**
 * Abstract implementation of {@link ICommandProcessor}.
 * This is required to handle parameters of a {@link Command} if provided by {@link CommandsConfiguration.CommandDefinition}.
 *
 * @author Anton Skripin
 */
public abstract class AbstractCommandProcessor implements ICommandProcessor {

    private final CommandsConfiguration commandsConfiguration;
    private final ITelegramClient telegramOutputGateway;
    private final CommandCache commandCache;

    /**
     * Constructor.
     *
     * @param commandsConfiguration See {@link CommandsConfiguration}
     * @param telegramOutputGateway See {@link ITelegramClient}
     * @param commandCache          See {@link CommandCache}
     */
    protected AbstractCommandProcessor(CommandsConfiguration commandsConfiguration,
                                       ITelegramClient telegramOutputGateway,
                                       CommandCache commandCache) {
        this.commandsConfiguration = commandsConfiguration;
        this.telegramOutputGateway = telegramOutputGateway;
        this.commandCache = commandCache;
    }

    @Override
    public void processCommand(CommandParameters commandParameters) {
        Command command = supportedCommand();
        AbstractCommandModel<?> model = supportedModelWithMapping();
        CommandState commandState = commandCache.getOrInit(command, model);

        if (commandParameters.navigation() != null) {
            handleNavigation(commandParameters, commandState);
        } else if (commandState.hasAwaitingKey()) {
            commandState.getCurrentCommandModel().append(commandState.getAwaitingKey(), commandParameters.input());
        }

        if (commandState.getCurrentCommandModel().getParamIterator().hasNext()) {
            promptNextParameter(command, commandState, commandParameters);
        } else {
            finalizeCommand(commandState, commandParameters);
        }
    }

    private void handleNavigation(CommandParameters commandParameters, CommandState commandState) {
        ListIterator<String> modelParamIterator = commandState.getCurrentCommandModel().getParamIterator();
        if (commandParameters.navigation().isNext() && modelParamIterator.hasNext()) {
            return;
        }

        if (commandParameters.navigation().isPrevious() && modelParamIterator.hasPrevious()) {
            commandState.setAwaitingKey(modelParamIterator.previous());
        }
        if (commandParameters.navigation().isPrevious() && modelParamIterator.hasPrevious()) {
            commandState.setAwaitingKey(modelParamIterator.previous());
        }
    }

    private void promptNextParameter(Command command, CommandState commandState, CommandParameters commandParameters) {
        String key = commandState.getCurrentCommandModel().getParamIterator().next();
        var currentParameter = commandsConfiguration.findParameter(command.getPath(), key);
        String prompt = currentParameter.prompt();
        commandState.setAwaitingKey(key);
        if (currentParameter.dynamicPrompt()) {
            provideDynamicPrompt(commandState.getCurrentCommandModel(), commandParameters);
        }
        if (commandsConfiguration.findCommand(command.getPath()).withNavigation()) {
            telegramOutputGateway.sendMessageWithNavigation(commandParameters.chatId(), prompt);
            return;
        }
        telegramOutputGateway.sendPlainMessage(commandParameters.chatId(), prompt);

    }

    private void finalizeCommand(CommandState commandState, CommandParameters commandParameters) {
        applyCommandModel(commandState.getCurrentCommandModel(), commandParameters);
        commandCache.clear();
    }
}
