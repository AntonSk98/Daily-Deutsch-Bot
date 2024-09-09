package com.ansk.development.learngermanwithansk98.service.impl.command.words;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.gateway.telegram.TelegramOutputGateway;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.repository.WordCache;
import com.ansk.development.learngermanwithansk98.service.impl.command.AbstractCommandService;
import com.ansk.development.learngermanwithansk98.service.impl.pipe.CardToImagesConverterPipe;
import com.ansk.development.learngermanwithansk98.service.model.*;
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
public class PreviewCachedWords extends AbstractCommandService {

    private final TelegramOutputGateway telegramOutputGateway;
    private final WordCache wordCache;
    private final CardToImagesConverterPipe converterPipe;

    protected PreviewCachedWords(CommandsConfiguration commandsConfiguration,
                                 TelegramOutputGateway telegramOutputGateway,
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
        return Command.PREVIEW;
    }

    @Override
    public void applyCommandModel(AbstractCommandModel<?> currentCommandModel, CommandParameters commandParameters) {
        List<Word> wordsInCache = wordCache.getWords();

        if (CollectionUtils.isEmpty(wordsInCache)) {
            telegramOutputGateway.sendPlainMessage(commandParameters.chatId(), "No words added in cache yet!");
            return;
        }

        WordCard previewWordCard = new WordCard("preview", wordsInCache);

        telegramOutputGateway.sendPlainMessage(commandParameters.chatId(), "Please wait...I am preparing the preview...");

        ExerciseDocument exerciseDocument = converterPipe.pipe(previewWordCard);

        telegramOutputGateway.sendWordCard(commandParameters.chatId(), exerciseDocument);
    }

    @Override
    public AbstractCommandModel<?> supportedCommandModel() {
        return new NoParamModel();
    }
}
