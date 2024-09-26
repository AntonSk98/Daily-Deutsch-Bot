package com.ansk.development.learngermanwithansk98.service.impl.command.reading;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.config.DailyDeutschBotConfiguration;
import com.ansk.development.learngermanwithansk98.gateway.telegram.ITelegramOutputGateway;
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
    private final ITelegramOutputGateway outputGateway;

    /**
     * Constructor.
     *
     * @param commandsConfiguration See {@link CommandsConfiguration}
     * @param telegramOutputGateway See {@link ITelegramOutputGateway}
     * @param commandCache          See {@link CommandCache}
     * @param botConfiguration      See {@link DailyDeutschBotConfiguration}
     */
    protected PublishReadingExercise(CommandsConfiguration commandsConfiguration,
                                     ITelegramOutputGateway telegramOutputGateway,
                                     CommandCache commandCache,
                                     DailyDeutschBotConfiguration botConfiguration,
                                     ReadingExerciseCache readingExerciseCache) {
        super(commandsConfiguration, telegramOutputGateway, commandCache, botConfiguration);
        this.readingExerciseCache = readingExerciseCache;
        this.outputGateway = telegramOutputGateway;
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
