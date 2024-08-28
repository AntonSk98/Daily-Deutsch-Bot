package com.ansk.development.learngermanwithansk98.service.impl.command;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.gateway.OutputGateway;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.repository.WordCache;
import com.ansk.development.learngermanwithansk98.service.model.*;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.ansk.development.learngermanwithansk98.service.impl.MapperUtils.map;
import static com.ansk.development.learngermanwithansk98.service.model.Command.DELETE_WORD;
import static java.lang.String.format;

/**
 * Service that deletes a {@link Word} from {@link WordCache}.
 *
 * @author Anton Skripin
 */
@Service
public class DeleteCachedWordCommandService extends AbstractCommandService {

    private final OutputGateway outputGateway;
    private final WordCache wordCache;

    protected DeleteCachedWordCommandService(CommandsConfiguration commandsConfiguration,
                                             OutputGateway outputGateway,
                                             CommandCache commandCache,
                                             WordCache wordCache) {
        super(commandsConfiguration, outputGateway, commandCache);
        this.outputGateway = outputGateway;
        this.wordCache = wordCache;
    }

    @Override
    public Command supportedCommand() {
        return DELETE_WORD;
    }

    @Override
    public void applyCommandModel(AbstractCommandModel<?> model, CommandParameters commandParameters) {
        map(wordCache.getWords())
                .stream()
                .map(wordInfo -> wordInfo.findByReference(ToBeDeletedWord.map(model).getWordReference()))
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
                            String deletedWord = ToBeDeletedWord.map(model).getWordReference();
                            outputGateway.sendPlainMessage(commandParameters.chatId(), format(message, deletedWord));
                        },
                        () -> {
                            String message = "Word '%s' not present in cache";
                            outputGateway.sendPlainMessage(commandParameters.chatId(), format(message, ToBeDeletedWord.map(model).getWordReference()));
                        });
    }

    @Override
    public AbstractCommandModel<?> supportedCommandModel() {
        return new ToBeDeletedWord()
                .init()
                .addMapping("word_reference", ToBeDeletedWord::setWordReference);
    }
}
