package com.ansk.development.learngermanwithansk98.service.impl.command.words;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.gateway.OutputGateway;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.repository.WordCache;
import com.ansk.development.learngermanwithansk98.service.impl.command.AbstractCommandService;
import com.ansk.development.learngermanwithansk98.service.model.Command;
import com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel;
import com.ansk.development.learngermanwithansk98.service.model.input.CommandParameters;
import com.ansk.development.learngermanwithansk98.service.model.input.NoParamModel;
import org.springframework.stereotype.Service;

/**
 * Commands that clear the {@link WordCache}.
 *
 * @author Anton Skripin
 */
@Service
public class ClearWordCache extends AbstractCommandService {

    private final OutputGateway outputGateway;
    private final WordCache wordCache;

    protected ClearWordCache(CommandsConfiguration commandsConfiguration,
                             OutputGateway outputGateway,
                             CommandCache commandCache,
                             WordCache wordCache) {
        super(commandsConfiguration, outputGateway, commandCache);
        this.outputGateway = outputGateway;
        this.wordCache = wordCache;
    }

    @Override
    public Command supportedCommand() {
        return Command.CLEAR_WORD_CACHE;
    }

    @Override
    public void applyCommandModel(AbstractCommandModel<?> currentCommandModel, CommandParameters parameters) {
        wordCache.cleanCache();
        outputGateway.sendPlainMessage(parameters.chatId(), "Word cache has been successfully cleared.");
    }

    @Override
    public AbstractCommandModel<?> supportedCommandModel() {
        return new NoParamModel();
    }
}
