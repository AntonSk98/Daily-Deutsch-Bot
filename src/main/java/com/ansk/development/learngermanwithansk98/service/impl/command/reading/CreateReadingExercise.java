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
import com.ansk.development.learngermanwithansk98.service.model.input.ReadingExerciseWithTextModel;
import com.ansk.development.learngermanwithansk98.service.model.output.ReadingExercise;
import org.springframework.stereotype.Service;

import static com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel.Properties.*;

/**
 * Service to create reading exercise based on the provided text.
 *
 * @author Anton Skripin
 */
@Service
public class CreateReadingExercise extends ReadingExerciseSupport {

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
    protected CreateReadingExercise(CommandsConfiguration commandsConfiguration,
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
        return Command.READING_EXERCISE_CREATE;
    }

    @Override
    public ReadingExercise.TextOutput getInputText(AbstractCommandModel<?> model, CommandParameters parameters) {
        ReadingExerciseWithTextModel readingExerciseModel = model.map(ReadingExerciseWithTextModel.class);
        return analyzeText(parameters, readingExerciseModel);
    }

    @Override
    public AbstractCommandModel<?> supportedModelWithMapping() {
        return new ReadingExerciseWithTextModel()
                .init()
                .addMapping(TEXT, ReadingExerciseWithTextModel::setText)
                .addMapping(SHOULD_DO, ReadingExerciseWithTextModel::parseValue);
    }

    private ReadingExercise.TextOutput analyzeText(CommandParameters parameters, ReadingExerciseWithTextModel model) {
        telegramClient.sendPlainMessage(parameters.chatId(), "Analyzing the text...");
        GenericPromptTemplate analyzeText = new GenericPromptTemplate(promptsConfiguration.rephraseText())
                .resolveVariable(TEXT, model.getText());
        var analyzedText = openAiClient.sendRequest(analyzeText.getPrompt(), ReadingExercise.TextOutput.class);
        telegramClient.sendPlainMessage(parameters.chatId(), "The text is successfully analyzed.");

        if (!model.shouldDo()) {
            return new ReadingExercise.TextOutput(analyzedText.level(), analyzedText.title(), model.getText());
        }

        return analyzedText;
    }
}
