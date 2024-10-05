package com.ansk.development.learngermanwithansk98.service.impl.command.writing;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.config.WritingPromptsConfiguration;
import com.ansk.development.learngermanwithansk98.integration.openai.OpenAiClient;
import com.ansk.development.learngermanwithansk98.integration.telegram.ITelegramClient;
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
     * @param telegramOutputGateway       See {@link ITelegramClient}
     * @param commandCache                See {@link CommandCache}
     * @param OpenAiClient                   See {@link OpenAiClient}
     * @param promptsConfiguration        See {@link WritingPromptsConfiguration}
     * @param writingExerciseDocumentPipe See {@link WritingExerciseDocumentPipe}
     * @param writingExerciseCache        See {@link WritingExerciseCache}
     */
    protected GenerateWritingExercise(CommandsConfiguration commandsConfiguration,
                                      ITelegramClient telegramOutputGateway,
                                      CommandCache commandCache,
                                      OpenAiClient OpenAiClient,
                                      WritingPromptsConfiguration promptsConfiguration,
                                      WritingExerciseDocumentPipe writingExerciseDocumentPipe,
                                      WritingExerciseCache writingExerciseCache) {
        super(commandsConfiguration,
                telegramOutputGateway,
                commandCache,
                OpenAiClient,
                promptsConfiguration,
                writingExerciseDocumentPipe,
                writingExerciseCache);
    }

    @Override
    public Command supportedCommand() {
        return WRITING_WITH_EXAMPLE;
    }

}
