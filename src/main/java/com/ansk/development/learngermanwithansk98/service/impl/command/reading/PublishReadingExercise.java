package com.ansk.development.learngermanwithansk98.service.impl.command.reading;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.config.DailyDeutschBotConfiguration;
import com.ansk.development.learngermanwithansk98.integration.telegram.ITelegramClient;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.repository.ReadingExerciseCache;
import com.ansk.development.learngermanwithansk98.service.impl.command.AbstractPublishExerciseSupport;
import com.ansk.development.learngermanwithansk98.service.model.Command;
import com.ansk.development.learngermanwithansk98.service.model.output.ReadingExercise;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.ansk.development.learngermanwithansk98.service.model.Command.READING_EXERCISE_PUBLISH;

/**
 * Service to publish a {@link ReadingExercise}.
 *
 * @author Anton Skripin
 */
@Service
public class PublishReadingExercise extends AbstractPublishExerciseSupport {

    private final ReadingExerciseCache readingExerciseCache;
    private final ITelegramClient outputGateway;

    /**
     * Constructor.
     *
     * @param commandsConfiguration See {@link CommandsConfiguration}
     * @param telegramClient See {@link ITelegramClient}
     * @param commandCache          See {@link CommandCache}
     * @param botConfiguration      See {@link DailyDeutschBotConfiguration}
     */
    protected PublishReadingExercise(CommandsConfiguration commandsConfiguration,
                                     ITelegramClient telegramClient,
                                     CommandCache commandCache,
                                     DailyDeutschBotConfiguration botConfiguration,
                                     ReadingExerciseCache readingExerciseCache) {
        super(commandsConfiguration, telegramClient, commandCache, botConfiguration);
        this.readingExerciseCache = readingExerciseCache;
        this.outputGateway = telegramClient;
    }

    @Override
    public Supplier<Boolean> isPresentInCache() {
        return () -> readingExerciseCache.cachedReadingExercise().isPresent();
    }

    @Override
    public Consumer<Long> publish() {
        return groupId -> outputGateway.sendReadingExercise(groupId, readingExerciseCache.cachedReadingExercise().orElseThrow());
    }

    @Override
    public Runnable clearCache() {
        return readingExerciseCache::clearCache;
    }

    @Override
    public Command supportedCommand() {
        return READING_EXERCISE_PUBLISH;
    }
}
