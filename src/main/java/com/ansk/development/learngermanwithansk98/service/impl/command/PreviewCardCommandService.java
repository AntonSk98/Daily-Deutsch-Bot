package com.ansk.development.learngermanwithansk98.service.impl.command;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.gateway.OutputGateway;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.repository.WordCache;
import com.ansk.development.learngermanwithansk98.service.impl.pipe.CardToImagesConverterPipe;
import com.ansk.development.learngermanwithansk98.service.model.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Service to preview currently cached {@link Word}s as a document.
 *
 * @author Anton Skripin
 */
@Service
public class PreviewCardCommandService extends AbstractCommandService {

    private final OutputGateway outputGateway;
    private final WordCache wordCache;
    private final CardToImagesConverterPipe converterPipe;

    protected PreviewCardCommandService(CommandsConfiguration commandsConfiguration,
                                        OutputGateway outputGateway,
                                        CommandCache commandCache,
                                        WordCache wordCache,
                                        CardToImagesConverterPipe converterPipe) {
        super(commandsConfiguration, outputGateway, commandCache);
        this.outputGateway = outputGateway;
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
            outputGateway.sendPlainMessage(commandParameters.chatId(), "No words added in cache yet!");
        }

        WordCard previewWordCard = new WordCard(RandomStringUtils.randomAlphabetic(10), "preview", wordsInCache);

        outputGateway.sendPlainMessage(commandParameters.chatId(), "Please wait...I am preparing the preview...");

        Images images = converterPipe.pipe(previewWordCard);

        outputGateway.sendImages(commandParameters.chatId(), images);
    }

    @Override
    public AbstractCommandModel<?> supportedCommandModel() {
        return new NoParamModel();
    }
}
