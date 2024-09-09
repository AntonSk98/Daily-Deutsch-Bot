package com.ansk.development.learngermanwithansk98.service.impl.command.writing;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.config.WritingPromptsConfiguration;
import com.ansk.development.learngermanwithansk98.gateway.openai.OpenAiGateway;
import com.ansk.development.learngermanwithansk98.gateway.telegram.ITelegramOutputGateway;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.repository.WritingExerciseCache;
import com.ansk.development.learngermanwithansk98.service.impl.command.AbstractCommandProcessor;
import com.ansk.development.learngermanwithansk98.service.impl.pipe.WritingExerciseDocumentPipe;
import com.ansk.development.learngermanwithansk98.service.model.GenericPromptTemplate;
import com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel;
import com.ansk.development.learngermanwithansk98.service.model.input.CommandParameters;
import com.ansk.development.learngermanwithansk98.service.model.input.WritingExerciseModel;
import com.ansk.development.learngermanwithansk98.service.model.output.ExerciseDocument;
import com.ansk.development.learngermanwithansk98.service.model.output.WritingExercise;

import static com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel.Properties.LEVEL;
import static com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel.Properties.TOPIC;

public abstract class WritingExerciseSupport extends AbstractCommandProcessor {

    private final ITelegramOutputGateway telegramOutputGateway;
    private final OpenAiGateway openAiGateway;
    private final WritingPromptsConfiguration promptsConfiguration;
    private final WritingExerciseDocumentPipe writingExerciseDocumentPipe;
    private final WritingExerciseCache writingExerciseCache;

    protected WritingExerciseSupport(CommandsConfiguration commandsConfiguration,
                                     ITelegramOutputGateway telegramOutputGateway,
                                     CommandCache commandCache,
                                     OpenAiGateway openAiGateway,
                                     WritingPromptsConfiguration promptsConfiguration,
                                     WritingExerciseDocumentPipe writingExerciseDocumentPipe,
                                     WritingExerciseCache writingExerciseCache) {
        super(commandsConfiguration, telegramOutputGateway, commandCache);
        this.telegramOutputGateway = telegramOutputGateway;
        this.openAiGateway = openAiGateway;
        this.promptsConfiguration = promptsConfiguration;
        this.writingExerciseDocumentPipe = writingExerciseDocumentPipe;
        this.writingExerciseCache = writingExerciseCache;
    }

    /**
     * Extension point to transform the generated writing exercise if required.
     *
     * @param writingExercise writing exercise
     * @return transformed writing exercise
     */
    WritingExercise.Output transform(WritingExercise.Output writingExercise) {
        return writingExercise;
    }

    @Override
    public void applyCommandModel(AbstractCommandModel<?> model, CommandParameters parameters) {
        WritingExerciseModel writingExerciseModel = model.map(WritingExerciseModel.class);
        GenericPromptTemplate createExercisePrompt = new GenericPromptTemplate(promptsConfiguration.writingExample());
        createExercisePrompt.resolveVariable(TOPIC, writingExerciseModel.getTopic());
        createExercisePrompt.resolveVariable(LEVEL, writingExerciseModel.getLevel());

        telegramOutputGateway.sendPlainMessage(parameters.chatId(), "Creating writing exercise...");

        WritingExercise.Output writingExercise = openAiGateway.sendRequest(createExercisePrompt.getPrompt(), WritingExercise.Output.class);

        writingExercise = transform(writingExercise);

        telegramOutputGateway.sendPlainMessage(parameters.chatId(), "Creating the document...");

        ExerciseDocument writingExerciseDocument = writingExerciseDocumentPipe.pipe(writingExercise);

        telegramOutputGateway.sendPlainMessage(parameters.chatId(), "Saving in cache...");

        writingExerciseCache.saveWritingExercise(new WritingExercise(writingExercise.topic(), writingExerciseDocument));

        telegramOutputGateway.sendPlainMessage(parameters.chatId(), "Created and saved!");
    }

    @Override
    public AbstractCommandModel<?> supportedModelWithMapping() {
        return new WritingExerciseModel()
                .init()
                .addMapping(LEVEL, WritingExerciseModel::setLevel)
                .addMapping(TOPIC, WritingExerciseModel::setTopic);
    }
}
