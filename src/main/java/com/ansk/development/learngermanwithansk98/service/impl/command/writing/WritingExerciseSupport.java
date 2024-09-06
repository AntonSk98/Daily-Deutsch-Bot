package com.ansk.development.learngermanwithansk98.service.impl.command.writing;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.config.WritingPromptsConfiguration;
import com.ansk.development.learngermanwithansk98.gateway.OutputGateway;
import com.ansk.development.learngermanwithansk98.gateway.openai.OpenAiGateway;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.service.impl.command.AbstractCommandService;
import com.ansk.development.learngermanwithansk98.service.impl.pipe.WritingExerciseDocumentPipe;
import com.ansk.development.learngermanwithansk98.service.model.GenericPromptTemplate;
import com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel;
import com.ansk.development.learngermanwithansk98.service.model.input.CommandParameters;
import com.ansk.development.learngermanwithansk98.service.model.input.WritingExerciseModel;
import com.ansk.development.learngermanwithansk98.service.model.output.Images;
import com.ansk.development.learngermanwithansk98.service.model.output.WritingExercise;

import static com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel.Properties.LEVEL;
import static com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel.Properties.TOPIC;

public abstract class WritingExerciseSupport extends AbstractCommandService {

    private final OutputGateway outputGateway;
    private final OpenAiGateway openAiGateway;
    private final WritingPromptsConfiguration promptsConfiguration;
    private final WritingExerciseDocumentPipe writingExerciseDocumentPipe;

    protected WritingExerciseSupport(CommandsConfiguration commandsConfiguration,
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

    /**
     * Extension point to transform the generated writing exercise if required.
     *
     * @param writingExercise writing exercise
     * @return transformed writing exercise
     */
    WritingExercise transform(WritingExercise writingExercise) {
        return writingExercise;
    }

    @Override
    public void applyCommandModel(AbstractCommandModel<?> model, CommandParameters parameters) {
        WritingExerciseModel writingExerciseModel = model.map(WritingExerciseModel.class);
        GenericPromptTemplate createExercisePrompt = new GenericPromptTemplate(promptsConfiguration.writingExample());
        createExercisePrompt.resolveVariable(TOPIC, writingExerciseModel.getTopic());
        createExercisePrompt.resolveVariable(LEVEL, writingExerciseModel.getLevel());

        outputGateway.sendPlainMessage(parameters.chatId(), "Creating writing exercise...");

        WritingExercise writingExercise = openAiGateway.sendRequest(createExercisePrompt.getPrompt(), WritingExercise.class);

        writingExercise = transform(writingExercise);

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
