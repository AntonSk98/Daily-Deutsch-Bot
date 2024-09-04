package com.ansk.development.learngermanwithansk98.service.impl.command.reading;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.config.ReadingPromptsConfiguration;
import com.ansk.development.learngermanwithansk98.gateway.OutputGateway;
import com.ansk.development.learngermanwithansk98.gateway.openai.OpenAiGateway;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.repository.ReadingExerciseCache;
import com.ansk.development.learngermanwithansk98.service.impl.command.AbstractCommandService;
import com.ansk.development.learngermanwithansk98.service.impl.pipe.ReadingExerciseDocumentPipe;
import com.ansk.development.learngermanwithansk98.service.model.GenericPromptTemplate;
import com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel;
import com.ansk.development.learngermanwithansk98.service.model.input.CommandParameters;
import com.ansk.development.learngermanwithansk98.service.model.output.Images;
import com.ansk.development.learngermanwithansk98.service.model.output.ReadingExercise;

import static com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel.Properties.TEXT;

/**
 * Contains common methods for creating any form of some reading exercise.
 *
 * @author Anton Skripin
 */
public abstract class ReadingExerciseSupport extends AbstractCommandService {

    private final OutputGateway outputGateway;
    private final OpenAiGateway aiGateway;
    private final ReadingPromptsConfiguration promptsConfiguration;
    private final ReadingExerciseDocumentPipe readingExerciseDocumentPipe;
    private final ReadingExerciseCache readingExerciseCache;

    protected ReadingExerciseSupport(CommandsConfiguration commandsConfiguration,
                                     OutputGateway outputGateway,
                                     CommandCache commandCache,
                                     OpenAiGateway aiGateway,
                                     ReadingPromptsConfiguration promptsConfiguration,
                                     ReadingExerciseDocumentPipe readingExerciseDocumentPipe,
                                     ReadingExerciseCache readingExerciseCache) {
        super(commandsConfiguration, outputGateway, commandCache);
        this.outputGateway = outputGateway;
        this.aiGateway = aiGateway;
        this.promptsConfiguration = promptsConfiguration;
        this.readingExerciseDocumentPipe = readingExerciseDocumentPipe;
        this.readingExerciseCache = readingExerciseCache;
    }

    public abstract ReadingExercise.TextOutput getInputText(AbstractCommandModel<?> model, CommandParameters parameters);

    @Override
    public void applyCommandModel(AbstractCommandModel<?> model, CommandParameters parameters) {
        var generatedText = getInputText(model, parameters);
        var tasks = generateTasks(parameters, generatedText);
        var paragraphs = mapToParagraphs(parameters, generatedText);
        var documentObject = generateDocument(parameters, generatedText, paragraphs, tasks);
        var document = pipeDocument(parameters, documentObject);

        var readingExercise = new ReadingExercise(
                generatedText.title(),
                paragraphs,
                tasks,
                document
        );

        readingExerciseCache.saveReadingExercise(readingExercise);

        outputGateway.sendPlainMessage(parameters.chatId(), "Reading exercise saved in cache!");
    }

    public ReadingExercise.ReadingTasks generateTasks(CommandParameters parameters, ReadingExercise.TextOutput generatedText) {
        outputGateway.sendPlainMessage(parameters.chatId(), "Creating reading exercise...");
        GenericPromptTemplate createExercise = new GenericPromptTemplate(promptsConfiguration.createReadingExercise())
                .resolveVariable(TEXT, generatedText.text());
        var tasks = aiGateway.sendRequest(createExercise.getPrompt(), ReadingExercise.ReadingTasks.class);
        outputGateway.sendPlainMessage(parameters.chatId(), "The exercise is generated.");
        return tasks;
    }

    public ReadingExercise.Paragraphs mapToParagraphs(CommandParameters parameters, ReadingExercise.TextOutput generatedText) {
        outputGateway.sendPlainMessage(parameters.chatId(), "Splitting the text into paragraphs....");
        GenericPromptTemplate textToParagraphsPrompt = new GenericPromptTemplate(promptsConfiguration.textToParagraphs())
                .resolveVariable(TEXT, generatedText.text());
        return aiGateway.sendRequest(textToParagraphsPrompt.getPrompt(), ReadingExercise.Paragraphs.class);
    }

    public ReadingExercise.Document generateDocument(CommandParameters parameters,
                                                     ReadingExercise.TextOutput generatedText,
                                                     ReadingExercise.Paragraphs paragraphs,
                                                     ReadingExercise.ReadingTasks tasks) {
        outputGateway.sendPlainMessage(parameters.chatId(), "Generating a document object.");
        return new ReadingExercise.Document(
                generatedText.level(),
                generatedText.title(),
                paragraphs.paragraphs(),
                tasks.tasks().stream().map(ReadingExercise.Task::question).toList()
        );
    }

    public Images pipeDocument(CommandParameters parameters, ReadingExercise.Document documentObject) {
        outputGateway.sendPlainMessage(parameters.chatId(), "Piping the document into media files...");
        return readingExerciseDocumentPipe.pipe(documentObject);
    }
}
