package com.ansk.development.learngermanwithansk98.gateway;

import com.ansk.development.learngermanwithansk98.service.api.ICommandService;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.service.model.Command;
import com.ansk.development.learngermanwithansk98.service.model.CommandParameters;
import com.ansk.development.learngermanwithansk98.service.model.Commands;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;
import java.util.Optional;


/**
 * A component that processes incoming messages by determining the appropriate {@link Command}.
 * Then the execution is delegated to the corresponding {@link ICommandService}.
 * <p>
 * {@link InputGateway} utilizes a cache to keep track of the current command and supports command execution based on the input text from a user.
 *
 * @author Anton Skripin
 */
@Component
public class InputGateway {

    private final List<ICommandService> commandServices;

    private final CommandCache commandCache;

    public InputGateway(List<ICommandService> commandServices,
                        CommandCache commandCache) {
        this.commandServices = commandServices;
        this.commandCache = commandCache;
    }

    public void process(Update update) {
        final String input = update.getMessage().getText();

        Command command = Commands.find(input)
                .or(() -> Optional.ofNullable(commandCache.getCurrentCommand()))
                .orElseThrow(() -> new IllegalStateException("Unknown command..."));

        commandCache.setCurrentCommand(command);
        ICommandService commandService = commandServices.stream()
                .filter(service -> service.supportedCommand().equals(command))
                .findFirst()
                .orElseThrow();

        commandService.execute(CommandParameters
                .create()
                .withChatId(update.getMessage().getChatId())
                .withInput(update.getMessage().getText()));
    }
}
