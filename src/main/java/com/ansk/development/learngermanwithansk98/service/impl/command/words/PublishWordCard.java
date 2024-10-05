package com.ansk.development.learngermanwithansk98.service.impl.command.words;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.config.DailyDeutschBotConfiguration;
import com.ansk.development.learngermanwithansk98.integration.telegram.ITelegramClient;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.repository.WordCache;
import com.ansk.development.learngermanwithansk98.service.impl.command.AbstractPublishExerciseSupport;
import com.ansk.development.learngermanwithansk98.service.impl.pipe.CardToImagesConverterPipe;
import com.ansk.development.learngermanwithansk98.service.model.Command;
import com.ansk.development.learngermanwithansk98.service.model.output.ExerciseDocument;
import com.ansk.development.learngermanwithansk98.service.model.output.WordCard;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.ansk.development.learngermanwithansk98.service.impl.MapperUtils.mapToDateGermanFormat;

/**
 * Service that publishes a word card to a group.
 *
 * @author Anton Skripin
 */
@Service
public class PublishWordCard extends AbstractPublishExerciseSupport {

    private final ITelegramClient telegramOutputGateway;
    private final WordCache wordCache;
    private final CardToImagesConverterPipe converterPipe;

    /**
     * Constructor.
     *
     * @param commandsConfiguration See {@link CommandsConfiguration}
     * @param telegramOutputGateway See {@link ITelegramClient}
     * @param commandCache          See {@link CommandCache}
     */
    protected PublishWordCard(CommandsConfiguration commandsConfiguration,
                              ITelegramClient telegramOutputGateway,
                              CommandCache commandCache,
                              DailyDeutschBotConfiguration botConfiguration,
                              WordCache wordCache,
                              CardToImagesConverterPipe converterPipe) {
        super(commandsConfiguration, telegramOutputGateway, commandCache, botConfiguration);
        this.telegramOutputGateway = telegramOutputGateway;
        this.wordCache = wordCache;
        this.converterPipe = converterPipe;
    }

    @Override
    public Command supportedCommand() {
        return Command.PUBLISH_WORD_CARD;
    }

    @Override
    public Supplier<Boolean> isPresentInCache() {
        return () -> !CollectionUtils.isEmpty(wordCache.getWords());
    }

    @Override
    public Consumer<Long> publish() {
        return groupId -> {
            WordCard previewWordCard = new WordCard(mapToDateGermanFormat(LocalDate.now()), wordCache.getWords());
            ExerciseDocument wordCardDocumentToPublish = converterPipe.pipe(previewWordCard);
            telegramOutputGateway.sendWordCard(groupId, wordCardDocumentToPublish);
        };
    }

    @Override
    public Runnable clearCache() {
        return wordCache::cleanCache;
    }
}
