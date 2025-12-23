package com.ansk.development.learngermanwithansk98.integration.telegram;

import static com.ansk.development.learngermanwithansk98.integration.telegram.TelegramMessageExtractor.chatId;
import static com.ansk.development.learngermanwithansk98.integration.telegram.TelegramMessageExtractor.input;

import com.ansk.development.learngermanwithansk98.exception.CommandExceptionHandler;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.service.api.ICommandHandler;
import com.ansk.development.learngermanwithansk98.service.model.Command;
import com.ansk.development.learngermanwithansk98.service.model.input.CommandParameters;
import java.util.List;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Receives incoming messages. Determines the appropriate command and delegates its execution to the
 * corresponding command handler.
 *
 * @author Anton Skripin
 */
@Component
public class InputGateway {

  private final List<ICommandHandler> commandHandlers;

  private final CommandCache commandCache;

  /**
   * Constructor.
   *
   * @param commandServices See {@link ICommandHandler}
   * @param commandCache See {@link CommandCache}
   */
  public InputGateway(
      List<ICommandHandler> commandServices,
      CommandCache commandCache,
      CommandExceptionHandler exceptionHandler) {
    this.commandHandlers = commandServices;
    this.commandCache = commandCache;
  }

  /**
   * Processes an {@link Update} received from Telegram Bot.
   *
   * @param update the {@link Update} current message from a user
   */
  public void process(Update update) {
    final long chatId = chatId(update);
    final String input = input(update);

    Command command =
        Command.find(input)
            .map(
                newCommand -> {
                  commandCache.clear();
                  commandCache.setCurrentCommand(newCommand);
                  return newCommand;
                })
            .orElseGet(
                () ->
                    commandCache
                        .getCurrentCommand()
                        .orElseThrow(
                            () ->
                                new IllegalStateException("Could not map to any known commands!")));

    commandHandlers.stream()
        .filter(handler -> handler.supportedCommand().equals(command))
        .findFirst()
        .orElseThrow()
        .processCommand(new CommandParameters(input, chatId));
  }
}
