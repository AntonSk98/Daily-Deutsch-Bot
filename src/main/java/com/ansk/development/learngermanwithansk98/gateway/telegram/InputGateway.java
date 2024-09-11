package com.ansk.development.learngermanwithansk98.gateway.telegram;

import com.ansk.development.learngermanwithansk98.exception.CommandExceptionHandler;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.service.api.ICommandProcessor;
import com.ansk.development.learngermanwithansk98.service.model.Command;
import com.ansk.development.learngermanwithansk98.service.model.input.CommandParameters;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Audio;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.List;
import java.util.Optional;

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
    private final CommandExceptionHandler exceptionHandler;

    /**
     * Constructor.
     *
     * @param commandServices  See {@link ICommandProcessor}
     * @param commandCache     See {@link CommandCache}
     * @param exceptionHandler See {@link CommandExceptionHandler}
     */
    public InputGateway(List<ICommandProcessor> commandServices,
                        CommandCache commandCache,
                        CommandExceptionHandler exceptionHandler) {
        this.commandServices = commandServices;
        this.commandCache = commandCache;
        this.exceptionHandler = exceptionHandler;
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
     * @param update the {@link Update} object containing the message or audio to be processed.
     */
    public void process(Update update) {
        final long chatId = Optional.ofNullable(update.getMessage())
                .map(Message::getChatId)
                .orElseGet(() -> update.getCallbackQuery().getMessage().getChatId());
        try {
            final String input = Optional.ofNullable(update.getMessage())
                    .map(Message::getText)
                    .or(() -> Optional.ofNullable(update.getMessage()).map(Message::getAudio).map(Audio::getFileId))
                    .orElse(null);



            Command command = Command.find(input)
                    .map(cmd -> {
                        commandCache.clear();
                        return cmd;
                    })
                    .or(() -> Optional.ofNullable(commandCache.getCurrentCommand()))
                    .map(cmd -> {
                        commandCache.setCurrentCommand(cmd);
                        return cmd;
                    })
                    .orElseThrow(() -> new IllegalStateException("Unknown command..."));


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
        } catch (Exception e) {
            exceptionHandler.handleGlobalException(chatId, e);
        }
    }
}
