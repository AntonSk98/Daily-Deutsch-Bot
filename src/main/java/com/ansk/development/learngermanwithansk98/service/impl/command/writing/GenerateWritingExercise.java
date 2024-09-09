package com.ansk.development.learngermanwithansk98.service.impl.command.writing;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.config.WritingPromptsConfiguration;
import com.ansk.development.learngermanwithansk98.gateway.openai.AIGateway;
import com.ansk.development.learngermanwithansk98.gateway.telegram.ITelegramOutputGateway;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.repository.WritingExerciseCache;
import com.ansk.development.learngermanwithansk98.service.impl.pipe.WritingExerciseDocumentPipe;
import com.ansk.development.learngermanwithansk98.service.model.Command;
import org.springframework.stereotype.Service;

import static com.ansk.development.learngermanwithansk98.service.model.Command.WRITING_WITH_EXAMPLE;

/**
 * Service to generate a writing exercise.
 *
 * @author Anton Skripin
 */
@Service
public class GenerateWritingExercise extends WritingExerciseSupport {

    /**
     * Constructor.
     *
     * @param commandsConfiguration       See {@link CommandsConfiguration}
     * @param telegramOutputGateway       See {@link ITelegramOutputGateway}
     * @param commandCache                See {@link CommandCache}
     * @param AIGateway                   See {@link AIGateway}
     * @param promptsConfiguration        See {@link WritingPromptsConfiguration}
     * @param writingExerciseDocumentPipe See {@link WritingExerciseDocumentPipe}
     * @param writingExerciseCache        See {@link WritingExerciseCache}
     */
    protected GenerateWritingExercise(CommandsConfiguration commandsConfiguration,
                                      ITelegramOutputGateway telegramOutputGateway,
                                      CommandCache commandCache,
                                      AIGateway AIGateway,
                                      WritingPromptsConfiguration promptsConfiguration,
                                      WritingExerciseDocumentPipe writingExerciseDocumentPipe,
                                      WritingExerciseCache writingExerciseCache) {
        super(commandsConfiguration,
                telegramOutputGateway,
                commandCache,
                AIGateway,
                promptsConfiguration,
                writingExerciseDocumentPipe,
                writingExerciseCache);
    }

    @Override
    public Command supportedCommand() {
        return WRITING_WITH_EXAMPLE;
    }

}
