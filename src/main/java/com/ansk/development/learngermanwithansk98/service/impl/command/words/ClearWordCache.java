package com.ansk.development.learngermanwithansk98.service.impl.command.words;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.gateway.telegram.ITelegramOutputGateway;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.repository.WordCache;
import com.ansk.development.learngermanwithansk98.service.impl.command.AbstractCommandProcessor;
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
public class ClearWordCache extends AbstractCommandProcessor {

    private final ITelegramOutputGateway telegramOutputGateway;
    private final WordCache wordCache;

    /**
     * Constructor.
     *
     * @param commandsConfiguration See {@link CommandsConfiguration}
     * @param telegramOutputGateway See {@link ITelegramOutputGateway}
     * @param commandCache          See {@link CommandCache}
     * @param wordCache             See {@link WordCache}
     */
    protected ClearWordCache(CommandsConfiguration commandsConfiguration,
                             ITelegramOutputGateway telegramOutputGateway,
                             CommandCache commandCache,
                             WordCache wordCache) {
        super(commandsConfiguration, telegramOutputGateway, commandCache);
        this.telegramOutputGateway = telegramOutputGateway;
        this.wordCache = wordCache;
    }

    @Override
    public Command supportedCommand() {
        return Command.CLEAR_WORD_CACHE;
    }

    @Override
    public void applyCommandModel(AbstractCommandModel<?> model, CommandParameters parameters) {
        wordCache.cleanCache();
        telegramOutputGateway.sendPlainMessage(parameters.chatId(), "Word cache has been successfully cleared.");
    }

    @Override
    public AbstractCommandModel<?> supportedModelWithMapping() {
        return new NoParamModel();
    }
}
