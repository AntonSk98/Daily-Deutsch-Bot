package com.ansk.development.learngermanwithansk98.service.impl.command.listening;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.integration.telegram.ITelegramClient;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.repository.ListeningExerciseCache;
import com.ansk.development.learngermanwithansk98.service.impl.command.AbstractCommandProcessor;
import com.ansk.development.learngermanwithansk98.service.model.Command;
import com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel;
import com.ansk.development.learngermanwithansk98.service.model.input.CommandParameters;
import com.ansk.development.learngermanwithansk98.service.model.input.NoParamModel;
import com.ansk.development.learngermanwithansk98.service.model.output.ListeningExercise;
import org.springframework.stereotype.Service;

/**
 * Service to preview a {@link ListeningExercise} saved int {@link ListeningExerciseCache}.
 *
 * @author Anton Skripin
 */
@Service
public class PreviewListeningExercise extends AbstractCommandProcessor {

    private final ITelegramClient telegramClient;
    private final ListeningExerciseCache listeningExerciseCache;

    /**
     * Constructor.
     *
     * @param commandsConfiguration  See {@link CommandsConfiguration}
     * @param telegramClient  See {@link ITelegramClient}
     * @param commandCache           See {@link CommandCache}
     * @param listeningExerciseCache See {@link ListeningExerciseCache}
     */
    protected PreviewListeningExercise(CommandsConfiguration commandsConfiguration,
                                       ITelegramClient telegramClient,
                                       CommandCache commandCache,
                                       ListeningExerciseCache listeningExerciseCache) {
        super(commandsConfiguration, telegramClient, commandCache);
        this.telegramClient = telegramClient;
        this.listeningExerciseCache = listeningExerciseCache;
    }

    @Override
    public Command supportedCommand() {
        return Command.LISTENING_EXERCISE_PREVIEW;
    }

    @Override
    public void applyCommandModel(AbstractCommandModel<?> model, CommandParameters parameters) {
        listeningExerciseCache.cachedListeningExercise().ifPresentOrElse(
                listeningExercise -> telegramClient.sendListeningExercise(parameters.chatId(), listeningExercise),
                () -> telegramClient.sendPlainMessage(parameters.chatId(), "No listening exercise in cache")
        );
    }

    @Override
    public AbstractCommandModel<?> supportedModelWithMapping() {
        return new NoParamModel();
    }
}
