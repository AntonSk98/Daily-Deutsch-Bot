package com.ansk.development.learngermanwithansk98.service.impl.command.writing;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.config.WritingPromptsConfiguration;
import com.ansk.development.learngermanwithansk98.integration.openai.OpenAiClient;
import com.ansk.development.learngermanwithansk98.integration.telegram.ITelegramClient;
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

/**
 * Utility class to generate writing exercise.
 *
 * @author Anton Skripin
 */
public abstract class WritingExerciseSupport extends AbstractCommandProcessor {

    private final ITelegramClient telegramOutputGateway;
    private final OpenAiClient OpenAiClient;
    private final WritingPromptsConfiguration promptsConfiguration;
    private final WritingExerciseDocumentPipe writingExerciseDocumentPipe;
    private final WritingExerciseCache writingExerciseCache;

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
    protected WritingExerciseSupport(CommandsConfiguration commandsConfiguration,
                                     ITelegramClient telegramOutputGateway,
                                     CommandCache commandCache,
                                     OpenAiClient OpenAiClient,
                                     WritingPromptsConfiguration promptsConfiguration,
                                     WritingExerciseDocumentPipe writingExerciseDocumentPipe,
                                     WritingExerciseCache writingExerciseCache) {
        super(commandsConfiguration, telegramOutputGateway, commandCache);
        this.telegramOutputGateway = telegramOutputGateway;
        this.OpenAiClient = OpenAiClient;
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

        WritingExercise.Output writingExercise = OpenAiClient.sendRequest(createExercisePrompt.getPrompt(), WritingExercise.Output.class);

        writingExercise = transform(writingExercise);

        telegramOutputGateway.sendPlainMessage(parameters.chatId(), "Creating the document...");

        ExerciseDocument writingExerciseDocument = writingExerciseDocumentPipe.pipe(writingExercise);

        telegramOutputGateway.sendPlainMessage(parameters.chatId(), "Saving in cache...");

        writingExerciseCache.saveWritingExercise(new WritingExercise(writingExercise.topic(), writingExercise.level(), writingExerciseDocument));

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
