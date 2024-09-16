package com.ansk.development.learngermanwithansk98.service.api;

import com.ansk.development.learngermanwithansk98.service.model.Command;
import com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel;
import com.ansk.development.learngermanwithansk98.service.model.input.CommandParameters;

/**
 * Interface that provides methods to process and execute a {@link Command}.
 *
 * @author Anton Skripin
 */
public interface ICommandProcessor {

    /**
     * Returns a {@link Command} that is supported by current command processor.
     *
     * @return supported command
     */
    Command supportedCommand();

    /**
     * Process a command giving a {@link CommandParameters}
     *
     * @param commandParameters {@link CommandParameters}
     */
    void processCommand(CommandParameters commandParameters);

    /**
     * Action that is to be executed as soon as a supported command model is fully fulfilled.
     *
     * @param model      model that is built during command flow
     * @param parameters parameters
     */
    void applyCommandModel(AbstractCommandModel<?> model, CommandParameters parameters);

    /**
     * Returns a mode that is supported by current command.
     * This method must provide mapping that maps an {@link AbstractCommandModel} to a concrete implementation.
     *
     * @return model
     */
    AbstractCommandModel<?> supportedModelWithMapping();

    /**
     * Provides the dynamic prompt required for a command parameter if it is set to be dynamic.
     *
     * @param parameters
     * @throws UnsupportedOperationException if the dynamic prompt is missing for the model even though the dynamic prompt is required
     */
    default void provideDynamicPrompt(CommandParameters parameters) {
        throw new UnsupportedOperationException("Parameter must provide a dynamic prompt but it is missing for the model!");
    }
}
