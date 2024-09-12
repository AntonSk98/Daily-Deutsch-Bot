package com.ansk.development.learngermanwithansk98.service.filter;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.exception.RequiredParameterException;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.repository.CommandState;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Checks whether a parameter is required and if it is the case then checks whether the value for the parameter is present.
 *
 * @author Anton Skripin
 */
@Component
@Order(3000)
public class RequiredParameterFilter implements IFilter {

    private final CommandCache commandCache;
    private final CommandsConfiguration commandsConfiguration;

    /**
     * Constructor.
     *
     * @param commandCache          See {@link CommandCache}
     * @param commandsConfiguration See {@link CommandsConfiguration}
     */
    public RequiredParameterFilter(CommandCache commandCache,
                                   CommandsConfiguration commandsConfiguration) {
        this.commandCache = commandCache;
        this.commandsConfiguration = commandsConfiguration;
    }

    @Override
    public void filter(FilterParameters parameters) {
        Optional<CommandState> currentCommandState = commandCache.findCurrentCommandState();

        if (currentCommandState.isEmpty()) {
            return;
        }

        if (StringUtils.isEmpty(currentCommandState.get().getAwaitingKey())) {
            return;
        }

        if (StringUtils.isNotEmpty(parameters.input())) {
            return;
        }

        if (currentCommandState.get().getCurrentCommandModel().isDefined(currentCommandState.get().getAwaitingKey())) {
            return;
        }

        if (!commandsConfiguration.findParameter(commandCache.getCurrentCommand().getPath(), currentCommandState.get().getAwaitingKey()).required()) {
            return;
        }

        throw new RequiredParameterException("Parameter '" + currentCommandState.get().getAwaitingKey() + " ' is a required parameter");
    }
}
