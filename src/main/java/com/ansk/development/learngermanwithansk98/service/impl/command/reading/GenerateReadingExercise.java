package com.ansk.development.learngermanwithansk98.service.impl.command.reading;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.config.ReadingPromptsConfiguration;
import com.ansk.development.learngermanwithansk98.integration.openai.OpenAiClient;
import com.ansk.development.learngermanwithansk98.integration.telegram.ITelegramClient;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.repository.ReadingExerciseCache;
import com.ansk.development.learngermanwithansk98.service.impl.pipe.ReadingExerciseDocumentPipe;
import com.ansk.development.learngermanwithansk98.service.model.Command;
import com.ansk.development.learngermanwithansk98.service.model.GenericPromptTemplate;
import com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel;
import com.ansk.development.learngermanwithansk98.service.model.input.CommandParameters;
import com.ansk.development.learngermanwithansk98.service.model.input.ReadingExerciseModel;
import com.ansk.development.learngermanwithansk98.service.model.output.ReadingExercise;
import org.springframework.stereotype.Service;

import static com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel.Properties.LEVEL;
import static com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel.Properties.TOPIC;

/**
 * Service to generate reading exercise based on the topic and the language level.
 *
 * @author Anton Skripin
 */
@Service
public class GenerateReadingExercise extends ReadingExerciseSupport {

    private final ITelegramClient telegramClient;
    private final OpenAiClient openAiClient;
    private final ReadingPromptsConfiguration promptsConfiguration;

    /**
     * Constructor.
     *
     * @param commandsConfiguration       See {@link CommandsConfiguration}
     * @param telegramClient       See {@link ITelegramClient}
     * @param commandCache                See {@link CommandCache}
     * @param openAiClient                   See {@link OpenAiClient}
     * @param promptsConfiguration        See {@link ReadingPromptsConfiguration}
     * @param readingExerciseDocumentPipe See {@link ReadingExerciseDocumentPipe}
     * @param readingExerciseCache        See {@link ReadingExerciseCache}
     */
    protected GenerateReadingExercise(CommandsConfiguration commandsConfiguration,
                                      ITelegramClient telegramClient,
                                      CommandCache commandCache,
                                      OpenAiClient openAiClient,
                                      ReadingPromptsConfiguration promptsConfiguration,
                                      ReadingExerciseDocumentPipe readingExerciseDocumentPipe,
                                      ReadingExerciseCache readingExerciseCache) {
        super(commandsConfiguration,
                telegramClient,
                commandCache,
                openAiClient,
                promptsConfiguration,
                readingExerciseDocumentPipe,
                readingExerciseCache);

        this.openAiClient = openAiClient;
        this.telegramClient = telegramClient;
        this.promptsConfiguration = promptsConfiguration;
    }

    @Override
    public Command supportedCommand() {
        return Command.READING_EXERCISE_GENERATE;
    }

    @Override
    public ReadingExercise.TextOutput getInputText(AbstractCommandModel<?> model, CommandParameters parameters) {
        ReadingExerciseModel readingExerciseModel = model.map(ReadingExerciseModel.class);
        return generateText(parameters, readingExerciseModel);
    }

    @Override
    public AbstractCommandModel<?> supportedModelWithMapping() {
        return new ReadingExerciseModel()
                .init()
                .addMapping(LEVEL, ReadingExerciseModel::setLevel)
                .addMapping(TOPIC, ReadingExerciseModel::setTopic);
    }

    private ReadingExercise.TextOutput generateText(CommandParameters parameters, ReadingExerciseModel readingExerciseModel) {
        telegramClient.sendPlainMessage(
                parameters.chatId(),
                String.format(
                        "Generating a text for level: %s. Topic of the text is '%s'",
                        readingExerciseModel.getLevel(),
                        readingExerciseModel.getTopic()
                )
        );
        GenericPromptTemplate generateText = new GenericPromptTemplate(promptsConfiguration.generateText())
                .resolveVariable(LEVEL, readingExerciseModel.getLevel())
                .resolveVariable(TOPIC, readingExerciseModel.getTopic());

        var generatedText = openAiClient.sendRequest(generateText.getPrompt(), ReadingExercise.TextOutput.class);
        telegramClient.sendPlainMessage(parameters.chatId(), "The text is generated.");
        return generatedText;
    }
}
