package com.ansk.development.learngermanwithansk98.service.impl.command.listening;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.config.DailyDeutschBotConfiguration;
import com.ansk.development.learngermanwithansk98.integration.telegram.ITelegramClient;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.repository.ListeningExerciseCache;
import com.ansk.development.learngermanwithansk98.service.impl.command.AbstractPublishExerciseSupport;
import com.ansk.development.learngermanwithansk98.service.model.Command;
import com.ansk.development.learngermanwithansk98.service.model.output.ListeningExercise;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.ansk.development.learngermanwithansk98.service.model.Command.PUBLISH_LISTENING_EXERCISE;

/**
 * Service to publish a {@link ListeningExercise}.
 *
 * @author Anton Skripin
 */
@Service
public class PublishListeningExercise extends AbstractPublishExerciseSupport {

    private final ListeningExerciseCache listeningExerciseCache;
    private final ITelegramClient outputGateway;

    /**
     * Constructor.
     *
     * @param commandsConfiguration See {@link CommandsConfiguration}
     * @param telegramClient See {@link ITelegramClient}
     * @param commandCache          See {@link CommandCache}
     * @param botConfiguration      See {@link DailyDeutschBotConfiguration}
     */
    protected PublishListeningExercise(CommandsConfiguration commandsConfiguration,
                                       ITelegramClient telegramClient,
                                       CommandCache commandCache,
                                       DailyDeutschBotConfiguration botConfiguration,
                                       ListeningExerciseCache listeningExerciseCache,
                                       ITelegramClient outputGateway) {
        super(commandsConfiguration, telegramClient, commandCache, botConfiguration);
        this.listeningExerciseCache = listeningExerciseCache;
        this.outputGateway = outputGateway;
    }

    @Override
    public Supplier<Boolean> isPresentInCache() {
        return () -> listeningExerciseCache.cachedListeningExercise().isPresent();
    }

    @Override
    public Consumer<Long> publish() {
        return groupId -> outputGateway.sendListeningExercise(groupId, listeningExerciseCache.cachedListeningExercise().orElseThrow());
    }

    @Override
    public Runnable clearCache() {
        return listeningExerciseCache::clearCache;
    }

    @Override
    public Command supportedCommand() {
        return PUBLISH_LISTENING_EXERCISE;
    }
}
