package com.ansk.development.learngermanwithansk98.service.impl.command.reading;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.gateway.telegram.TelegramOutputGateway;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.repository.ReadingExerciseCache;
import com.ansk.development.learngermanwithansk98.service.impl.command.AbstractCommandService;
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
public class PreviewReadingExercise extends AbstractCommandService {

    private final TelegramOutputGateway telegramOutputGateway;
    private final ReadingExerciseCache readingExerciseCache;

    protected PreviewReadingExercise(CommandsConfiguration commandsConfiguration,
                                     TelegramOutputGateway telegramOutputGateway,
                                     CommandCache commandCache,
                                     ReadingExerciseCache readingExerciseCache) {
        super(commandsConfiguration, telegramOutputGateway, commandCache);
        this.telegramOutputGateway = telegramOutputGateway;
        this.readingExerciseCache = readingExerciseCache;
    }

    @Override
    public Command supportedCommand() {
        return Command.READING_EXERCISE_PREVIEW;
    }

    @Override
    public void applyCommandModel(AbstractCommandModel<?> currentCommandModel, CommandParameters paramaters) {
        readingExerciseCache.cachedReadingExercise().ifPresentOrElse(
                readingExercise -> {
                    telegramOutputGateway.sendPlainMessage(paramaters.chatId(), "Preparing reading exercise for preview");
                    telegramOutputGateway.sendReadingExercise(paramaters.chatId(), readingExercise);

                },
                () -> telegramOutputGateway.sendPlainMessage(paramaters.chatId(), "No reading exercise is in cache currently")
        );
    }

    @Override
    public AbstractCommandModel<?> supportedCommandModel() {
        return new NoParamModel();
    }
}
