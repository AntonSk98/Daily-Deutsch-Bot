package com.ansk.development.learngermanwithansk98.service.impl.command.words;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.gateway.telegram.ITelegramOutputGateway;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.repository.WordCache;
import com.ansk.development.learngermanwithansk98.service.impl.command.AbstractCommandProcessor;
import com.ansk.development.learngermanwithansk98.service.impl.pipe.CardToImagesConverterPipe;
import com.ansk.development.learngermanwithansk98.service.model.Command;
import com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel;
import com.ansk.development.learngermanwithansk98.service.model.input.CommandParameters;
import com.ansk.development.learngermanwithansk98.service.model.input.NoParamModel;
import com.ansk.development.learngermanwithansk98.service.model.input.Word;
import com.ansk.development.learngermanwithansk98.service.model.output.ExerciseDocument;
import com.ansk.development.learngermanwithansk98.service.model.output.WordCard;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Service to preview currently cached {@link Word}s as a document.
 *
 * @author Anton Skripin
 */
@Service
public class PreviewCachedWords extends AbstractCommandProcessor {

    private final ITelegramOutputGateway telegramOutputGateway;
    private final WordCache wordCache;
    private final CardToImagesConverterPipe converterPipe;

    /**
     * Constructor.
     *
     * @param commandsConfiguration See {@link CommandsConfiguration}
     * @param telegramOutputGateway See {@link ITelegramOutputGateway}
     * @param commandCache          See {@link CommandCache}
     * @param wordCache             See {@link WordCache}
     * @param converterPipe         See {@link CardToImagesConverterPipe}
     */
    protected PreviewCachedWords(CommandsConfiguration commandsConfiguration,
                                 ITelegramOutputGateway telegramOutputGateway,
                                 CommandCache commandCache,
                                 WordCache wordCache,
                                 CardToImagesConverterPipe converterPipe) {
        super(commandsConfiguration, telegramOutputGateway, commandCache);
        this.telegramOutputGateway = telegramOutputGateway;
        this.wordCache = wordCache;
        this.converterPipe = converterPipe;
    }

    @Override
    public Command supportedCommand() {
        return Command.PREVIEW_WORD_CARD;
    }

    @Override
    public void applyCommandModel(AbstractCommandModel<?> model, CommandParameters parameters) {
        List<Word> wordsInCache = wordCache.getWords();

        if (CollectionUtils.isEmpty(wordsInCache)) {
            telegramOutputGateway.sendPlainMessage(parameters.chatId(), "No words added in cache yet!");
            return;
        }

        WordCard previewWordCard = new WordCard("preview", wordsInCache);

        telegramOutputGateway.sendPlainMessage(parameters.chatId(), "Please wait...I am preparing the preview...");

        ExerciseDocument exerciseDocument = converterPipe.pipe(previewWordCard);

        telegramOutputGateway.sendWordCard(parameters.chatId(), exerciseDocument);
    }

    @Override
    public AbstractCommandModel<?> supportedModelWithMapping() {
        return new NoParamModel();
    }
}
