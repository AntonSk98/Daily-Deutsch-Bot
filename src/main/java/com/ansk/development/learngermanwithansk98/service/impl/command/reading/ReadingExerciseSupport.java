package com.ansk.development.learngermanwithansk98.service.impl.command.reading;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.config.ReadingPromptsConfiguration;
import com.ansk.development.learngermanwithansk98.integration.openai.OpenAiClient;
import com.ansk.development.learngermanwithansk98.integration.telegram.ITelegramClient;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.repository.ReadingExerciseCache;
import com.ansk.development.learngermanwithansk98.service.impl.command.AbstractCommandProcessor;
import com.ansk.development.learngermanwithansk98.service.impl.pipe.ReadingExerciseDocumentPipe;
import com.ansk.development.learngermanwithansk98.service.model.GenericPromptTemplate;
import com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel;
import com.ansk.development.learngermanwithansk98.service.model.input.CommandParameters;
import com.ansk.development.learngermanwithansk98.service.model.output.ExerciseDocument;
import com.ansk.development.learngermanwithansk98.service.model.output.ReadingExercise;

import java.util.Objects;

import static com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel.Properties.TEXT;

/**
 * Contains common methods for creating any form of a reading exercise.
 *
 * @author Anton Skripin
 */
public abstract class ReadingExerciseSupport extends AbstractCommandProcessor {

    private final ITelegramClient telegramClient;
    private final OpenAiClient openAiClient;
    private final ReadingPromptsConfiguration promptsConfiguration;
    private final ReadingExerciseDocumentPipe readingExerciseDocumentPipe;
    private final ReadingExerciseCache readingExerciseCache;

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
    protected ReadingExerciseSupport(CommandsConfiguration commandsConfiguration,
                                     ITelegramClient telegramClient,
                                     CommandCache commandCache,
                                     OpenAiClient openAiClient,
                                     ReadingPromptsConfiguration promptsConfiguration,
                                     ReadingExerciseDocumentPipe readingExerciseDocumentPipe,
                                     ReadingExerciseCache readingExerciseCache) {
        super(commandsConfiguration, telegramClient, commandCache);
        this.telegramClient = telegramClient;
        this.openAiClient = openAiClient;
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

        Objects.requireNonNull(paragraphs);
        Objects.requireNonNull(paragraphs.paragraphs());
        Objects.requireNonNull(tasks);
        Objects.requireNonNull(tasks.tasks());

        var readingExercise = new ReadingExercise(
                generatedText.title(),
                documentObject.level(),
                paragraphs,
                tasks,
                document
        );

        readingExerciseCache.saveReadingExercise(readingExercise);

        telegramClient.sendPlainMessage(parameters.chatId(), "Reading exercise saved in cache!");
    }

    /**
     * Generates exercise task
     *
     * @param parameters parameters
     * @param text       input text
     * @return reading exercise tasks
     */
    public ReadingExercise.ReadingTasks generateTasks(CommandParameters parameters, ReadingExercise.TextOutput text) {
        telegramClient.sendPlainMessage(parameters.chatId(), "Creating reading exercise...");
        GenericPromptTemplate createExercise = new GenericPromptTemplate(promptsConfiguration.createReadingExercise())
                .resolveVariable(TEXT, text.text());
        var tasks = openAiClient.sendRequest(createExercise.getPrompt(), ReadingExercise.ReadingTasks.class);
        telegramClient.sendPlainMessage(parameters.chatId(), "The exercise is generated.");
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
        telegramClient.sendPlainMessage(parameters.chatId(), "Splitting the text into paragraphs....");
        GenericPromptTemplate textToParagraphsPrompt = new GenericPromptTemplate(promptsConfiguration.textToParagraphs())
                .resolveVariable(TEXT, text.text());
        return openAiClient.sendRequest(textToParagraphsPrompt.getPrompt(), ReadingExercise.Paragraphs.class);
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
        telegramClient.sendPlainMessage(parameters.chatId(), "Generating a document object.");
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
        telegramClient.sendPlainMessage(parameters.chatId(), "Piping the document into media files...");
        return readingExerciseDocumentPipe.pipe(documentMetadata);
    }
}
