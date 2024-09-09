package com.ansk.development.learngermanwithansk98.service.impl.command;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.gateway.telegram.TelegramOutputGateway;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.repository.CommandState;
import com.ansk.development.learngermanwithansk98.service.api.ICommandService;
import com.ansk.development.learngermanwithansk98.service.model.Command;
import com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel;
import com.ansk.development.learngermanwithansk98.service.model.input.CommandParameters;
import org.apache.commons.lang3.StringUtils;

import java.util.ListIterator;

/**
 * Abstract implementation of {@link ICommandService}.
 * This is required to handle parameters of a {@link Command} if provided by {@link CommandsConfiguration.CommandDefinition}.
 *
 * @author Anton Skripin
 */
public abstract class AbstractCommandService implements ICommandService {

    private final CommandsConfiguration commandsConfiguration;
    private final TelegramOutputGateway telegramOutputGateway;
    private final CommandCache commandCache;

    protected AbstractCommandService(CommandsConfiguration commandsConfiguration,
                                     TelegramOutputGateway telegramOutputGateway,
                                     CommandCache commandCache) {
        this.commandsConfiguration = commandsConfiguration;
        this.telegramOutputGateway = telegramOutputGateway;
        this.commandCache = commandCache;
    }

    @Override
    public void processCommand(CommandParameters commandParameters) {
        Command command = supportedCommand();
        AbstractCommandModel<?> model = supportedCommandModel();
        CommandState commandState = commandCache.getOrInit(command, model);

        // todo move it to a separate filter ?
        if (StringUtils.isNotEmpty(commandState.getAwaitingKey())
                && StringUtils.isEmpty(commandParameters.input())
                && !commandState.getCurrentCommandModel().isDefined(commandState.getAwaitingKey())
                && commandsConfiguration.findParameter(command.getPath(), commandState.getAwaitingKey()).required()
        ) {
            telegramOutputGateway.sendPlainMessage(commandParameters.chatId(), "This is a required parameter");
            return;
        }

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
        String prompt = commandsConfiguration.findParameter(command.getPath(), key).prompt();
        commandState.setAwaitingKey(key);
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
