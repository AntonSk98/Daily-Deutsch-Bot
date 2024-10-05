package com.ansk.development.learngermanwithansk98.service.impl.command.writing;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.integration.telegram.ITelegramClient;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.repository.WritingExerciseCache;
import com.ansk.development.learngermanwithansk98.service.impl.command.AbstractCommandProcessor;
import com.ansk.development.learngermanwithansk98.service.model.Command;
import com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel;
import com.ansk.development.learngermanwithansk98.service.model.input.CommandParameters;
import com.ansk.development.learngermanwithansk98.service.model.input.NoParamModel;
import com.ansk.development.learngermanwithansk98.service.model.output.WritingExercise;
import org.springframework.stereotype.Service;

/**
 * Service to preview {@link WritingExercise} from a {@link WritingExerciseCache}.
 *
 * @author Anton Skripin
 */
@Service
public class PreviewWritingExercise extends AbstractCommandProcessor {

    private final ITelegramClient telegramOutputGateway;
    private final WritingExerciseCache writingExerciseCache;

    /**
     * Constructor.
     *
     * @param commandsConfiguration {@link CommandsConfiguration}
     * @param telegramOutputGateway {@link ITelegramClient}
     * @param commandCache          {@link CommandCache}
     * @param writingExerciseCache  {@link WritingExerciseCache}
     */
    protected PreviewWritingExercise(CommandsConfiguration commandsConfiguration,
                                     ITelegramClient telegramOutputGateway,
                                     CommandCache commandCache,
                                     WritingExerciseCache writingExerciseCache) {
        super(commandsConfiguration, telegramOutputGateway, commandCache);
        this.telegramOutputGateway = telegramOutputGateway;
        this.writingExerciseCache = writingExerciseCache;
    }

    @Override
    public Command supportedCommand() {
        return Command.WRITING_EXERCISE_PREVIEW;
    }

    @Override
    public void applyCommandModel(AbstractCommandModel<?> model, CommandParameters parameters) {
        writingExerciseCache.cachedWritingExercise().ifPresentOrElse(
                writingExercise -> telegramOutputGateway.sendWritingExercise(parameters.chatId(), writingExercise),
                () -> telegramOutputGateway.sendPlainMessage(parameters.chatId(), "No writing exercise in cache.")
        );
    }

    @Override
    public AbstractCommandModel<?> supportedModelWithMapping() {
        return new NoParamModel();
    }
}
