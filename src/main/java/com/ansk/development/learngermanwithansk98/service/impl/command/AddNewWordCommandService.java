package com.ansk.development.learngermanwithansk98.service.impl.command;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.gateway.OutputGateway;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.repository.WordCache;
import com.ansk.development.learngermanwithansk98.service.model.AbstractCommandModel;
import com.ansk.development.learngermanwithansk98.service.model.Command;
import com.ansk.development.learngermanwithansk98.service.model.CommandParameters;
import com.ansk.development.learngermanwithansk98.service.model.Word;
import org.springframework.stereotype.Service;

import static com.ansk.development.learngermanwithansk98.service.model.Command.ADD_NEW_WORD;

/**
 * Service that adds a new {@link Word} to {@link WordCache}.
 *
 * @author Anton Skripin
 */
@Service
public class AddNewWordCommandService extends AbstractCommandService {

    private final OutputGateway outputGateway;
    private final WordCache wordCache;

    protected AddNewWordCommandService(CommandsConfiguration commandsConfiguration,
                                       OutputGateway outputGateway,
                                       CommandCache commandCache,
                                       WordCache wordCache) {
        super(commandsConfiguration, outputGateway, commandCache);
        this.outputGateway = outputGateway;
        this.wordCache = wordCache;
    }

    @Override
    public Command supportedCommand() {
        return ADD_NEW_WORD;
    }

    @Override
    public void applyCommandModel(AbstractCommandModel<?> model, CommandParameters commandParameters) {
        wordCache.addWord(Word.map(model));
        outputGateway.sendMessageWithPayload(commandParameters.chatId(), "Word saved to cache!", model);
    }

    @Override
    public AbstractCommandModel<?> supportedCommandModel() {
        return new Word()
                .init()
                .addMapping("word", Word::setWord)
                .addMapping("translation", Word::setTranslation)
                .addMapping("meaning", Word::setMeaning)
                .addMapping("forms", Word::setForms)
                .addMapping("frequency", (word, frequency) -> word.setFrequency(Integer.parseInt(frequency)))
                .addMapping("example", Word::setExample)
                .addMapping("example-translation", Word::setExampleTranslation);
    }
}
