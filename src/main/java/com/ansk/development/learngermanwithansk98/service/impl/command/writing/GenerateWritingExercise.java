package com.ansk.development.learngermanwithansk98.service.impl.command.writing;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.config.WritingPromptsConfiguration;
import com.ansk.development.learngermanwithansk98.gateway.OutputGateway;
import com.ansk.development.learngermanwithansk98.gateway.openai.OpenAiGateway;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
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


    protected GenerateWritingExercise(CommandsConfiguration commandsConfiguration,
                                      OutputGateway outputGateway,
                                      CommandCache commandCache,
                                      OpenAiGateway openAiGateway,
                                      WritingPromptsConfiguration promptsConfiguration,
                                      WritingExerciseDocumentPipe writingExerciseDocumentPipe) {
        super(commandsConfiguration, outputGateway, commandCache, openAiGateway, promptsConfiguration, writingExerciseDocumentPipe);
    }

    @Override
    public Command supportedCommand() {
        return WRITING_WITH_EXAMPLE;
    }

}
