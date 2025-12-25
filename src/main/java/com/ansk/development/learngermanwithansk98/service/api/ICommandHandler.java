package com.ansk.development.learngermanwithansk98.service.api;

import com.ansk.development.learngermanwithansk98.service.model.Command;
import com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel;
import com.ansk.development.learngermanwithansk98.service.model.input.CommandParameters;

/**
 * Interface that provides methods to process and execute a {@link Command}.
 *
 * @author Anton Skripin
 */
public interface ICommandHandler {

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
   * @param model model that is built during command flow
   * @param parameters parameters
   */
  void applyCommandModel(AbstractCommandModel<?> model, CommandParameters parameters);

  /**
   * Returns a model that is supported by current command. This method must provide mapping that
   * maps an {@link AbstractCommandModel} to a concrete implementation.
   *
   * @return model
   */
  AbstractCommandModel<?> supportedModelWithMapping();

  /**
   * Provides context for the current prompt if the parameter requires dynamic context.
   *
   * @param currentModelState current state of a model. Since the command is not yet complete the
   *     state is not complete as well
   * @param parameters parameters
   * @throws UnsupportedOperationException if the configured context is missing for the command key
   */
  default void providePromptContext(
      AbstractCommandModel<?> currentModelState, CommandParameters parameters) {
    throw new UnsupportedOperationException("Parameter must provide the context for the prompt!");
  }
}
