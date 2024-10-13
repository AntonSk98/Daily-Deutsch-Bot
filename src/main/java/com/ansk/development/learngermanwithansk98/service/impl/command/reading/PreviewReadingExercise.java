package com.ansk.development.learngermanwithansk98.service.impl.command.reading;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.integration.telegram.ITelegramClient;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.repository.ReadingExerciseCache;
import com.ansk.development.learngermanwithansk98.service.impl.command.AbstractCommandProcessor;
import com.ansk.development.learngermanwithansk98.service.model.Command;
import com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel;
import com.ansk.development.learngermanwithansk98.service.model.input.CommandParameters;
import com.ansk.development.learngermanwithansk98.service.model.input.NoParamModel;
import com.ansk.development.learngermanwithansk98.service.model.output.ReadingExercise;
import org.springframework.stereotype.Service;

/**
 * Service that sends a preview of the {@link ReadingExercise} that is currently saved in {@link ReadingExerciseCache}.
 *
 * @author Anton Skripin
 */
@Service
public class PreviewReadingExercise extends AbstractCommandProcessor {

    private final ITelegramClient telegramClient;
    private final ReadingExerciseCache readingExerciseCache;

    /**
     * Constructor.
     *
     * @param commandsConfiguration See {@link CommandsConfiguration}
     * @param telegramClient See {@link ITelegramClient}
     * @param commandCache          See {@link CommandCache}
     * @param readingExerciseCache  See {@link ReadingExerciseCache}
     */
    protected PreviewReadingExercise(CommandsConfiguration commandsConfiguration,
                                     ITelegramClient telegramClient,
                                     CommandCache commandCache,
                                     ReadingExerciseCache readingExerciseCache) {
        super(commandsConfiguration, telegramClient, commandCache);
        this.telegramClient = telegramClient;
        this.readingExerciseCache = readingExerciseCache;
    }

    @Override
    public Command supportedCommand() {
        return Command.READING_EXERCISE_PREVIEW;
    }

    @Override
    public void applyCommandModel(AbstractCommandModel<?> model, CommandParameters parameters) {
        readingExerciseCache.cachedReadingExercise().ifPresentOrElse(
                readingExercise -> {
                    telegramClient.sendPlainMessage(parameters.chatId(), "Preparing reading exercise for preview");
                    telegramClient.sendReadingExercise(parameters.chatId(), readingExercise);

                },
                () -> telegramClient.sendPlainMessage(parameters.chatId(), "No reading exercise is in cache currently")
        );
    }

    @Override
    public AbstractCommandModel<?> supportedModelWithMapping() {
        return new NoParamModel();
    }
}
