package com.ansk.development.learngermanwithansk98.service.impl.command;

import com.ansk.development.learngermanwithansk98.config.ReadingPromptsConfiguration;
import com.ansk.development.learngermanwithansk98.gateway.OutputGateway;
import com.ansk.development.learngermanwithansk98.gateway.openai.OpenAiGateway;
import com.ansk.development.learngermanwithansk98.service.impl.pipe.ReadingExerciseDocumentPipe;
import com.ansk.development.learngermanwithansk98.service.model.GenericPromptTemplate;
import com.ansk.development.learngermanwithansk98.service.model.input.CommandParameters;
import com.ansk.development.learngermanwithansk98.service.model.output.Images;
import com.ansk.development.learngermanwithansk98.service.model.output.ReadingExercise;

import static com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel.Properties.TEXT;

/**
 * Contains common methods for creating any form of some reading exercise.
 *
 * @author Anton Skripin
 */
public class ReadingExerciseSupport {

    private final OutputGateway outputGateway;
    private final OpenAiGateway aiGateway;
    private final ReadingExerciseDocumentPipe readingExercisePipe;
    private final ReadingPromptsConfiguration readingPromptsConfiguration;

    public ReadingExerciseSupport(OutputGateway outputGateway,
                                  OpenAiGateway aiGateway,
                                  ReadingExerciseDocumentPipe readingExercisePipe,
                                  ReadingPromptsConfiguration readingPromptsConfiguration) {
        this.outputGateway = outputGateway;
        this.aiGateway = aiGateway;
        this.readingExercisePipe = readingExercisePipe;
        this.readingPromptsConfiguration = readingPromptsConfiguration;
    }

    public ReadingExercise.ReadingTasks generateTasks(CommandParameters parameters, ReadingExercise.TextOutput generatedText) {
        outputGateway.sendPlainMessage(parameters.chatId(), "Creating reading exercise...");
        GenericPromptTemplate createExercise = new GenericPromptTemplate(readingPromptsConfiguration.createReadingExercise())
                .resolveVariable(TEXT, generatedText.text());
        var tasks = aiGateway.sendRequest(createExercise.getPrompt(), ReadingExercise.ReadingTasks.class);
        outputGateway.sendPlainMessage(parameters.chatId(), "The exercise is generated.");
        return tasks;
    }

    public ReadingExercise.Paragraphs mapToParagraphs(CommandParameters parameters, ReadingExercise.TextOutput generatedText) {
        outputGateway.sendPlainMessage(parameters.chatId(), "Splitting the text into paragraphs....");
        GenericPromptTemplate textToParagraphsPrompt = new GenericPromptTemplate(readingPromptsConfiguration.textToParagraphs())
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
        return readingExercisePipe.pipe(documentObject);
    }
}
