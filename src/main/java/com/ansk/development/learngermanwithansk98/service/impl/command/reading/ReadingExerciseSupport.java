package com.ansk.development.learngermanwithansk98.service.impl.command.reading;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.config.ReadingPromptsConfiguration;
import com.ansk.development.learngermanwithansk98.gateway.openai.AIGateway;
import com.ansk.development.learngermanwithansk98.gateway.telegram.ITelegramOutputGateway;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.repository.ReadingExerciseCache;
import com.ansk.development.learngermanwithansk98.service.impl.command.AbstractCommandProcessor;
import com.ansk.development.learngermanwithansk98.service.impl.pipe.ReadingExerciseDocumentPipe;
import com.ansk.development.learngermanwithansk98.service.model.GenericPromptTemplate;
import com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel;
import com.ansk.development.learngermanwithansk98.service.model.input.CommandParameters;
import com.ansk.development.learngermanwithansk98.service.model.output.ExerciseDocument;
import com.ansk.development.learngermanwithansk98.service.model.output.ReadingExercise;

import static com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel.Properties.TEXT;

/**
 * Contains common methods for creating any form of a reading exercise.
 *
 * @author Anton Skripin
 */
public abstract class ReadingExerciseSupport extends AbstractCommandProcessor {

    private final ITelegramOutputGateway telegramOutputGateway;
    private final AIGateway aiGateway;
    private final ReadingPromptsConfiguration promptsConfiguration;
    private final ReadingExerciseDocumentPipe readingExerciseDocumentPipe;
    private final ReadingExerciseCache readingExerciseCache;

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
    protected ReadingExerciseSupport(CommandsConfiguration commandsConfiguration,
                                     ITelegramOutputGateway telegramOutputGateway,
                                     CommandCache commandCache,
                                     AIGateway aiGateway,
                                     ReadingPromptsConfiguration promptsConfiguration,
                                     ReadingExerciseDocumentPipe readingExerciseDocumentPipe,
                                     ReadingExerciseCache readingExerciseCache) {
        super(commandsConfiguration, telegramOutputGateway, commandCache);
        this.telegramOutputGateway = telegramOutputGateway;
        this.aiGateway = aiGateway;
        this.promptsConfiguration = promptsConfiguration;
        this.readingExerciseDocumentPipe = readingExerciseDocumentPipe;
        this.readingExerciseCache = readingExerciseCache;
    }

    /**
     * Returns an input text for provided {@code model}.
     *
     * @param model      reading exercise model
     * @param parameters parameters
     * @return text
     */
    public abstract ReadingExercise.TextOutput getInputText(AbstractCommandModel<?> model, CommandParameters parameters);

    @Override
    public void applyCommandModel(AbstractCommandModel<?> model, CommandParameters parameters) {
        var generatedText = getInputText(model, parameters);
        var tasks = generateTasks(parameters, generatedText);
        var paragraphs = mapToParagraphs(parameters, generatedText);
        var documentObject = generateDocumentMetadata(parameters, generatedText, paragraphs, tasks);
        var document = pipeDocument(parameters, documentObject);

        var readingExercise = new ReadingExercise(
                generatedText.title(),
                paragraphs,
                tasks,
                document
        );

        readingExerciseCache.saveReadingExercise(readingExercise);

        telegramOutputGateway.sendPlainMessage(parameters.chatId(), "Reading exercise saved in cache!");
    }

    /**
     * Generates exercise task
     *
     * @param parameters parameters
     * @param text       input text
     * @return reading exercise tasks
     */
    public ReadingExercise.ReadingTasks generateTasks(CommandParameters parameters, ReadingExercise.TextOutput text) {
        telegramOutputGateway.sendPlainMessage(parameters.chatId(), "Creating reading exercise...");
        GenericPromptTemplate createExercise = new GenericPromptTemplate(promptsConfiguration.createReadingExercise())
                .resolveVariable(TEXT, text.text());
        var tasks = aiGateway.sendRequest(createExercise.getPrompt(), ReadingExercise.ReadingTasks.class);
        telegramOutputGateway.sendPlainMessage(parameters.chatId(), "The exercise is generated.");
        return tasks;
    }

    /**
     * Splits a text into paragraphs.
     *
     * @param parameters parameters
     * @param text       text
     * @return paragraphs
     */
    public ReadingExercise.Paragraphs mapToParagraphs(CommandParameters parameters, ReadingExercise.TextOutput text) {
        telegramOutputGateway.sendPlainMessage(parameters.chatId(), "Splitting the text into paragraphs....");
        GenericPromptTemplate textToParagraphsPrompt = new GenericPromptTemplate(promptsConfiguration.textToParagraphs())
                .resolveVariable(TEXT, text.text());
        return aiGateway.sendRequest(textToParagraphsPrompt.getPrompt(), ReadingExercise.Paragraphs.class);
    }

    /**
     * Generates a document metadata with reading exercise.
     *
     * @param parameters parameters
     * @param text       text
     * @param paragraphs paragraphs
     * @param tasks      tasks
     * @return reading exercise document
     */
    public ReadingExercise.Document generateDocumentMetadata(CommandParameters parameters,
                                                             ReadingExercise.TextOutput text,
                                                             ReadingExercise.Paragraphs paragraphs,
                                                             ReadingExercise.ReadingTasks tasks) {
        telegramOutputGateway.sendPlainMessage(parameters.chatId(), "Generating a document object.");
        return new ReadingExercise.Document(
                text.level(),
                text.title(),
                paragraphs.paragraphs(),
                tasks.tasks().stream().map(ReadingExercise.Task::question).toList()
        );
    }

    /**
     * Pipes reading exercise document metadata into document.
     *
     * @param parameters       parameters
     * @param documentMetadata document metadata
     * @return exercise document
     */
    public ExerciseDocument pipeDocument(CommandParameters parameters, ReadingExercise.Document documentMetadata) {
        telegramOutputGateway.sendPlainMessage(parameters.chatId(), "Piping the document into media files...");
        return readingExerciseDocumentPipe.pipe(documentMetadata);
    }
}
