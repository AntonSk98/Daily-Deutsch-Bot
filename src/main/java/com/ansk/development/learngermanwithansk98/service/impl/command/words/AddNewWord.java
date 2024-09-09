package com.ansk.development.learngermanwithansk98.service.impl.command.words;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.gateway.telegram.ITelegramOutputGateway;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.repository.WordCache;
import com.ansk.development.learngermanwithansk98.service.impl.command.AbstractCommandProcessor;
import com.ansk.development.learngermanwithansk98.service.model.Command;
import com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel;
import com.ansk.development.learngermanwithansk98.service.model.input.CommandParameters;
import com.ansk.development.learngermanwithansk98.service.model.input.Word;
import org.springframework.stereotype.Service;

import static com.ansk.development.learngermanwithansk98.service.model.Command.ADD_NEW_WORD;
import static com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel.Properties.*;

/**
 * Service that adds a new {@link Word} to {@link WordCache}.
 *
 * @author Anton Skripin
 */
@Service
public class AddNewWord extends AbstractCommandProcessor {

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
    protected AddNewWord(CommandsConfiguration commandsConfiguration,
                         ITelegramOutputGateway telegramOutputGateway,
                         CommandCache commandCache,
                         WordCache wordCache) {
        super(commandsConfiguration, telegramOutputGateway, commandCache);
        this.telegramOutputGateway = telegramOutputGateway;
        this.wordCache = wordCache;
    }

    @Override
    public Command supportedCommand() {
        return ADD_NEW_WORD;
    }

    @Override
    public void applyCommandModel(AbstractCommandModel<?> model, CommandParameters parameters) {
        wordCache.addWord(model.map(Word.class));
        telegramOutputGateway.sendMessageWithPayload(parameters.chatId(), "Word saved to cache!", model);
    }

    @Override
    public AbstractCommandModel<?> supportedModelWithMapping() {
        return new Word()
                .init()
                .addMapping(WORD, Word::setWord)
                .addMapping(TRANSLATION, Word::setTranslation)
                .addMapping(MEANING, Word::setMeaning)
                .addMapping(FORMS, Word::setForms)
                .addMapping(FREQUENCY, (word, frequency) -> word.setFrequency(Integer.parseInt(frequency)))
                .addMapping(EXAMPLE, Word::setExample)
                .addMapping(EXAMPLE_TRANSLATION, Word::setExampleTranslation);
    }
}
