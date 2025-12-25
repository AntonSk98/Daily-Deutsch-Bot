package com.ansk.development.learngermanwithansk98.service.impl.command;

import com.ansk.development.learngermanwithansk98.config.CommandsConfigurationProperties;
import com.ansk.development.learngermanwithansk98.integration.telegram.ITelegramClient;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.repository.CommandState;
import com.ansk.development.learngermanwithansk98.service.api.ICommandHandler;
import com.ansk.development.learngermanwithansk98.service.model.Command;
import com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel;
import com.ansk.development.learngermanwithansk98.service.model.input.CommandParameters;

/**
 * Abstract implementation of {@link ICommandHandler}. This is required to handle parameters of a
 * {@link Command} if provided by {@link CommandsConfigurationProperties.CommandDefinition}.
 *
 * @author Anton Skripin
 */
public abstract class AbstractCommandProcessor implements ICommandHandler {

  private final CommandsConfigurationProperties commandsConfiguration;
  private final ITelegramClient telegramClient;
  private final CommandCache commandCache;

  /**
   * Constructor.
   *
   * @param commandsConfiguration See {@link CommandsConfigurationProperties}
   * @param telegramClient See {@link ITelegramClient}
   * @param commandCache See {@link CommandCache}
   */
  protected AbstractCommandProcessor(
      CommandsConfigurationProperties commandsConfiguration,
      ITelegramClient telegramClient,
      CommandCache commandCache) {
    this.commandsConfiguration = commandsConfiguration;
    this.telegramClient = telegramClient;
    this.commandCache = commandCache;
  }

  @Override
  public void processCommand(CommandParameters commandParameters) {
    Command command = supportedCommand();
    AbstractCommandModel<?> model = supportedModelWithMapping();
    CommandState commandState = commandCache.getOrInit(command, model);

    if (commandState.hasAwaitingKey()) {
      commandState
          .getCurrentCommandModel()
          .setKeyValue(commandState.getAwaitingKey(), commandParameters.input());
    }

    if (commandState.getCurrentCommandModel().getParamIterator().hasNext()) {
      promptNextParameter(command, commandState, commandParameters);
    } else {
      finalizeCommand(commandState, commandParameters);
    }
  }

  private void promptNextParameter(
      Command command, CommandState commandState, CommandParameters commandParameters) {
    String key = commandState.getCurrentCommandModel().getParamIterator().next();
    var currentParameter = commandsConfiguration.findParameter(command.getPath(), key);
    String prompt = currentParameter.prompt();
    commandState.setAwaitingKey(key);
    if (currentParameter.withContext()) {
      providePromptContext(commandState.getCurrentCommandModel(), commandParameters);
    }
    telegramClient.sendPlainMessage(commandParameters.chatId(), prompt);
  }

  private void finalizeCommand(CommandState commandState, CommandParameters commandParameters) {
    applyCommandModel(commandState.getCurrentCommandModel(), commandParameters);
    commandCache.clear();
  }
}
