package com.ansk.development.learngermanwithansk98.service.impl.command.words;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.integration.telegram.ITelegramClient;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.repository.WordCache;
import com.ansk.development.learngermanwithansk98.service.impl.command.AbstractCommandProcessor;
import com.ansk.development.learngermanwithansk98.service.model.Command;
import com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel;
import com.ansk.development.learngermanwithansk98.service.model.input.CommandParameters;
import com.ansk.development.learngermanwithansk98.service.model.input.ToBeDeletedWord;
import com.ansk.development.learngermanwithansk98.service.model.input.Word;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.ansk.development.learngermanwithansk98.service.impl.MapperUtils.map;
import static com.ansk.development.learngermanwithansk98.service.model.Command.DELETE_WORD;
import static com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel.Properties.WORD_REFERENCE;
import static java.lang.String.format;

/**
 * Service that deletes a {@link Word} from {@link WordCache}.
 *
 * @author Anton Skripin
 */
@Service
public class DeleteCachedWord extends AbstractCommandProcessor {

    private final ITelegramClient telegramOutputGateway;
    private final WordCache wordCache;

    /**
     * Constructor.
     *
     * @param commandsConfiguration See {@link CommandsConfiguration}
     * @param telegramOutputGateway See {@link ITelegramClient}
     * @param commandCache          See {@link CommandCache}
     * @param wordCache             See {@link WordCache}
     */
    protected DeleteCachedWord(CommandsConfiguration commandsConfiguration,
                               ITelegramClient telegramOutputGateway,
                               CommandCache commandCache,
                               WordCache wordCache) {
        super(commandsConfiguration, telegramOutputGateway, commandCache);
        this.telegramOutputGateway = telegramOutputGateway;
        this.wordCache = wordCache;
    }

    @Override
    public Command supportedCommand() {
        return DELETE_WORD;
    }

    @Override
    public void applyCommandModel(AbstractCommandModel<?> model, CommandParameters parameters) {
        map(wordCache.getWords())
                .stream()
                .map(wordInfo -> wordInfo.findByReference(model.map(ToBeDeletedWord.class).getWordReference()))
                .flatMap(Optional::stream)
                .map(wordInfo -> wordCache.getWords()
                        .stream()
                        .filter(word -> word.getWord().equals(wordInfo.word()))
                        .filter(word -> word.getTranslation().equals(wordInfo.translation()))
                        .findFirst())
                .flatMap(Optional::stream)
                .findFirst()
                .ifPresentOrElse(word -> {
                            wordCache.deleteWord(word);
                            String message = "Successfully deleted the word '%s'";
                            String deletedWord = model.map(ToBeDeletedWord.class).getWordReference();
                            telegramOutputGateway.sendPlainMessage(parameters.chatId(), format(message, deletedWord));
                        },
                        () -> {
                            String message = "Word '%s' not present in cache";
                            telegramOutputGateway.sendPlainMessage(parameters.chatId(), format(message, model.map(ToBeDeletedWord.class).getWordReference()));
                        });
    }

    @Override
    public AbstractCommandModel<?> supportedModelWithMapping() {
        return new ToBeDeletedWord()
                .init()
                .addMapping(WORD_REFERENCE, ToBeDeletedWord::setWordReference);
    }
}
