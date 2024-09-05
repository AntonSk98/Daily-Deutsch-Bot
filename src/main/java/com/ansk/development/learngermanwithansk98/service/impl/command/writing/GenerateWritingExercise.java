package com.ansk.development.learngermanwithansk98.service.impl.command.writing;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.config.WritingPromptsConfiguration;
import com.ansk.development.learngermanwithansk98.gateway.OutputGateway;
import com.ansk.development.learngermanwithansk98.gateway.openai.OpenAiGateway;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.service.impl.command.AbstractCommandService;
import com.ansk.development.learngermanwithansk98.service.impl.pipe.WritingExerciseDocumentPipe;
import com.ansk.development.learngermanwithansk98.service.model.Command;
import com.ansk.development.learngermanwithansk98.service.model.GenericPromptTemplate;
import com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel;
import com.ansk.development.learngermanwithansk98.service.model.input.CommandParameters;
import com.ansk.development.learngermanwithansk98.service.model.input.WritingExerciseModel;
import com.ansk.development.learngermanwithansk98.service.model.output.Images;
import com.ansk.development.learngermanwithansk98.service.model.output.WritingExercise;
import org.springframework.stereotype.Service;

import static com.ansk.development.learngermanwithansk98.service.model.Command.WRITING_WITH_EXAMPLE;
import static com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel.Properties.LEVEL;
import static com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel.Properties.TOPIC;

/**
 * Service to generate a writing exercise.
 *
 * @author Anton Skripin
 */
@Service
public class GenerateWritingExercise extends AbstractCommandService {

    private final OutputGateway outputGateway;
    private final OpenAiGateway openAiGateway;
    private final WritingPromptsConfiguration promptsConfiguration;
    private final WritingExerciseDocumentPipe writingExerciseDocumentPipe;

    protected GenerateWritingExercise(CommandsConfiguration commandsConfiguration,
                                      OutputGateway outputGateway,
                                      CommandCache commandCache,
                                      OpenAiGateway openAiGateway,
                                      WritingPromptsConfiguration promptsConfiguration,
                                      WritingExerciseDocumentPipe writingExerciseDocumentPipe) {
        super(commandsConfiguration, outputGateway, commandCache);
        this.outputGateway = outputGateway;
        this.openAiGateway = openAiGateway;
        this.promptsConfiguration = promptsConfiguration;
        this.writingExerciseDocumentPipe = writingExerciseDocumentPipe;
    }

    @Override
    public Command supportedCommand() {
        return WRITING_WITH_EXAMPLE;
    }

    @Override
    public void applyCommandModel(AbstractCommandModel<?> model, CommandParameters parameters) {
        WritingExerciseModel writingExerciseModel = model.map(WritingExerciseModel.class);
        GenericPromptTemplate createWrtingExercise = new GenericPromptTemplate(promptsConfiguration.writingExample());
        createWrtingExercise.resolveVariable(TOPIC, writingExerciseModel.getTopic());
        createWrtingExercise.resolveVariable(LEVEL, writingExerciseModel.getLevel());

        outputGateway.sendPlainMessage(parameters.chatId(), "Creating writing exercise...");

        WritingExercise writingExercise = openAiGateway.sendRequest(createWrtingExercise.getPrompt(), WritingExercise.class);

        outputGateway.sendPlainMessage(parameters.chatId(), "Creating the document...");

        Images writingExerciseDocument = writingExerciseDocumentPipe.pipe(writingExercise);

        outputGateway.sendPlainMessage(parameters.chatId(), "Saving in cache...");

        outputGateway.sendWritingExercise(parameters.chatId(), writingExercise.topic(), writingExerciseDocument);

        outputGateway.sendPlainMessage(parameters.chatId(), "Created and saved!");
    }

    @Override
    public AbstractCommandModel<?> supportedCommandModel() {
        return new WritingExerciseModel()
                .init()
                .addMapping(LEVEL, WritingExerciseModel::setLevel)
                .addMapping(TOPIC, WritingExerciseModel::setTopic);
    }
}
