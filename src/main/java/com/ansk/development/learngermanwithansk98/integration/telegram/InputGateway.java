package com.ansk.development.learngermanwithansk98.integration.telegram;

import com.ansk.development.learngermanwithansk98.exception.CommandExceptionHandler;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.service.api.ICommandProcessor;
import com.ansk.development.learngermanwithansk98.service.model.Command;
import com.ansk.development.learngermanwithansk98.service.model.input.CommandParameters;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

import static com.ansk.development.learngermanwithansk98.integration.telegram.TelegramMapper.chatId;
import static com.ansk.development.learngermanwithansk98.integration.telegram.TelegramMapper.input;
import static com.ansk.development.learngermanwithansk98.service.impl.MapperUtils.map;


/**
 * A component that processes incoming messages by determining the appropriate {@link Command}.
 * <p>
 * The {@link InputGateway} class delegates the execution of commands to the corresponding {@link ICommandProcessor}.
 * It uses a cache to manage the current command and supports text and audio inputs from users.
 *
 * @author Anton Skripin
 */
@Component
public class InputGateway {

    private final List<ICommandProcessor> commandServices;

    private final CommandCache commandCache;

    /**
     * Constructor.
     *
     * @param commandServices See {@link ICommandProcessor}
     * @param commandCache    See {@link CommandCache}
     */
    public InputGateway(List<ICommandProcessor> commandServices,
                        CommandCache commandCache,
                        CommandExceptionHandler exceptionHandler) {
        this.commandServices = commandServices;
        this.commandCache = commandCache;
    }

    /**
     * Processes an {@link Update} received from Telegram.
     * <p>
     * This method extracts the input -> determines the appropriate {@link Command} ->
     * -> delegates the execution to the corresponding {@link ICommandProcessor}.
     * <p>
     * The state of the command is managed  {@link CommandCache}.
     * </p>
     *
     * @param update the {@link Update} current message from a user
     */
    public void process(Update update) {
        final long chatId = chatId(update);
        final String input = input(update);

        Command command = commandCache.getCurrentCommand();


        commandServices
                .stream()
                .filter(service -> service.supportedCommand().equals(command))
                .findFirst()
                .orElseThrow()
                .processCommand(CommandParameters.create()
                        .withChatId(chatId)
                        .withInput(input)
                        .addNavigation(map(update))
                );
    }
}
