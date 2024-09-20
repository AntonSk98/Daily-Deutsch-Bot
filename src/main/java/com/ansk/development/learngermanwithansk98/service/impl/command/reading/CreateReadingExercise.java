package com.ansk.development.learngermanwithansk98.service.impl.command.reading;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.config.ReadingPromptsConfiguration;
import com.ansk.development.learngermanwithansk98.gateway.openai.AIGateway;
import com.ansk.development.learngermanwithansk98.gateway.telegram.ITelegramOutputGateway;
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

    private final ITelegramOutputGateway telegramOutputGateway;
    private final AIGateway aiGateway;
    private final ReadingPromptsConfiguration promptsConfiguration;

    /**
     * Constructor.
     *
     * @param commandsConfiguration       See {@link CommandsConfiguration}
     * @param telegramOutputGateway       See {@link ITelegramOutputGateway}
     * @param commandCache                See {@link CommandCache}
     * @param aiGateway                   See {@link AIGateway}
     * @param promptsConfiguration        See {@link ReadingPromptsConfiguration}
     * @param readingExerciseDocumentPipe See {@link ReadingExerciseDocumentPipe}
     * @param readingExerciseCache        See {@link ReadingExerciseCache}
     */
    protected CreateReadingExercise(CommandsConfiguration commandsConfiguration,
                                    ITelegramOutputGateway telegramOutputGateway,
                                    CommandCache commandCache,
                                    AIGateway aiGateway,
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
                .addMapping(SHOULD_REPHRASE_TEXT, (model, value) -> model.shouldRephrase(value.contains(APPROVE_PROMPT)));
    }

    private ReadingExercise.TextOutput analyzeText(CommandParameters parameters, ReadingExerciseWithTextModel model) {
        telegramOutputGateway.sendPlainMessage(parameters.chatId(), "Analyzing the text...");
        GenericPromptTemplate analyzeText = new GenericPromptTemplate(promptsConfiguration.rephraseText())
                .resolveVariable(TEXT, model.getText());
        var analyzedText = aiGateway.sendRequest(analyzeText.getPrompt(), ReadingExercise.TextOutput.class);
        telegramOutputGateway.sendPlainMessage(parameters.chatId(), "The text is successfully analyzed.");

        if (!model.shouldRephrase()) {
            return new ReadingExercise.TextOutput(analyzedText.level(), analyzedText.title(), model.getText());
        }

        return analyzedText;
    }
}
