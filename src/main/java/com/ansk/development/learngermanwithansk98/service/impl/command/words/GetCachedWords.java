package com.ansk.development.learngermanwithansk98.service.impl.command.words;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.gateway.telegram.ITelegramOutputGateway;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.repository.WordCache;
import com.ansk.development.learngermanwithansk98.service.impl.command.AbstractCommandProcessor;
import com.ansk.development.learngermanwithansk98.service.model.*;
import com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel;
import com.ansk.development.learngermanwithansk98.service.model.input.CommandParameters;
import com.ansk.development.learngermanwithansk98.service.model.input.NoParamModel;
import com.ansk.development.learngermanwithansk98.service.model.input.Word;
import com.ansk.development.learngermanwithansk98.service.model.output.WordInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

import static com.ansk.development.learngermanwithansk98.service.impl.MapperUtils.map;

/**
 * Service that returns currently cached {@link Word}s.
 *
 * @author Anton Skripin
 */
@Service
public class GetCachedWords extends AbstractCommandProcessor {

    private final WordCache wordCache;
    private final ITelegramOutputGateway telegramOutputGateway;

    protected GetCachedWords(CommandsConfiguration commandsConfiguration,
                             ITelegramOutputGateway telegramOutputGateway,
                             CommandCache commandCache,
                             WordCache wordCache) {
        super(commandsConfiguration, telegramOutputGateway, commandCache);
        this.wordCache = wordCache;
        this.telegramOutputGateway = telegramOutputGateway;
    }

    @Override
    public Command supportedCommand() {
        return Command.GET_WORDS;
    }

    @Override
    public void applyCommandModel(AbstractCommandModel<?> model, CommandParameters parameters) {
        Collection<WordInfo> wordInfoCollection = map(wordCache.getWords());
        String wordInfoString = wordInfoCollection.stream().map(WordInfo::prettyPrint).collect(Collectors.joining("\n"));
        String message = StringUtils.isEmpty(wordInfoString) ? "No words in cache yet" : wordInfoString;
        telegramOutputGateway.sendPlainMessage(parameters.chatId(), message);
    }

    @Override
    public AbstractCommandModel<?> supportedModelWithMapping() {
        return new NoParamModel();
    }
}
