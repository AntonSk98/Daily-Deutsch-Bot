package com.ansk.development.learngermanwithansk98.service.impl.command.reading;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.config.ReadingPromptsConfiguration;
import com.ansk.development.learngermanwithansk98.gateway.telegram.ITelegramOutputGateway;
import com.ansk.development.learngermanwithansk98.gateway.openai.OpenAiGateway;
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
 * Service to generate reading exercise.
 *
 * @author Anton Skripin
 */
@Service
public class GenerateReadingExercise extends ReadingExerciseSupport {

    private final ITelegramOutputGateway telegramOutputGateway;
    private final OpenAiGateway aiGateway;
    private final ReadingPromptsConfiguration promptsConfiguration;


    protected GenerateReadingExercise(CommandsConfiguration commandsConfiguration,
                                      ITelegramOutputGateway telegramOutputGateway,
                                      CommandCache commandCache,
                                      OpenAiGateway aiGateway,
                                      ReadingPromptsConfiguration promptsConfiguration,
                                      ReadingExerciseDocumentPipe readingExerciseDocumentPipe,
                                      ReadingExerciseCache readingExerciseCache) {
        super(commandsConfiguration,
                telegramOutputGateway,
                commandCache,
                aiGateway,
                promptsConfiguration,
                readingExerciseDocumentPipe,
                readingExerciseCache);

        this.aiGateway = aiGateway;
        this.telegramOutputGateway = telegramOutputGateway;
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

    private ReadingExercise.TextOutput generateText(CommandParameters parameters, ReadingExerciseModel readingExerciseModel) {
        telegramOutputGateway.sendPlainMessage(
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

        var generatedText = aiGateway.sendRequest(generateText.getPrompt(), ReadingExercise.TextOutput.class);
        telegramOutputGateway.sendPlainMessage(parameters.chatId(), "The text is generated.");
        return generatedText;
    }

    @Override
    public AbstractCommandModel<?> supportedCommandModel() {
        return new ReadingExerciseModel()
                .init()
                .addMapping(LEVEL, ReadingExerciseModel::setLevel)
                .addMapping(TOPIC, ReadingExerciseModel::setTopic);
    }
}
