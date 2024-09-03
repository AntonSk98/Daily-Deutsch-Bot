package com.ansk.development.learngermanwithansk98.service.impl.command;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.config.ReadingPromptsConfiguration;
import com.ansk.development.learngermanwithansk98.gateway.OutputGateway;
import com.ansk.development.learngermanwithansk98.gateway.openai.OpenAiGateway;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.service.impl.pipe.ReadingExerciseDocumentPipe;
import com.ansk.development.learngermanwithansk98.service.model.Command;
import com.ansk.development.learngermanwithansk98.service.model.GenericPromptTemplate;
import com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel;
import com.ansk.development.learngermanwithansk98.service.model.input.CommandParameters;
import com.ansk.development.learngermanwithansk98.service.model.input.ReadingExerciseModel;
import com.ansk.development.learngermanwithansk98.service.model.output.Images;
import com.ansk.development.learngermanwithansk98.service.model.output.ReadingExercise;
import org.springframework.stereotype.Service;

/**
 * Service to generate reading exercise.
 *
 * @author Anton Skripin
 */
@Service
public class CreateReadingExerciseCommand extends AbstractCommandService {


    private final OpenAiGateway aiGateway;
    private final OutputGateway outputGateway;
    private final ReadingPromptsConfiguration readingPromptsConfiguration;
    private final ReadingExerciseDocumentPipe readingExercisePipe;

    private static final String LEVEL = "level";
    private static final String TOPIC = "topic";
    private static final String TEXT = "text";

    protected CreateReadingExerciseCommand(CommandsConfiguration commandsConfiguration,
                                           OutputGateway outputGateway,
                                           OpenAiGateway aiGateway,
                                           CommandCache commandCache,
                                           ReadingPromptsConfiguration readingPromptsConfiguration,
                                           ReadingExerciseDocumentPipe readingExercisePipe) {
        super(commandsConfiguration, outputGateway, commandCache);
        this.aiGateway = aiGateway;
        this.outputGateway = outputGateway;
        this.readingPromptsConfiguration = readingPromptsConfiguration;
        this.readingExercisePipe = readingExercisePipe;
    }

    @Override
    public Command supportedCommand() {
        return Command.READING_EXERCISE;
    }

    @Override
    public void applyCommandModel(AbstractCommandModel<?> model, CommandParameters parameters) {
        ReadingExerciseModel readingExerciseModel = model.map(ReadingExerciseModel.class);
        var generatedText = generateText(parameters, readingExerciseModel);
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

        outputGateway.sendReadingExercise(parameters.chatId(), readingExercise);

    }

    private ReadingExercise.TextOutput generateText(CommandParameters parameters, ReadingExerciseModel readingExerciseModel) {
        outputGateway.sendPlainMessage(
                parameters.chatId(),
                String.format(
                        "Generating a text for level: %s. Topic of the text is '%s'",
                        readingExerciseModel.getLevel(),
                        readingExerciseModel.getTopic()
                )
        );
        GenericPromptTemplate generateText = new GenericPromptTemplate(readingPromptsConfiguration.generateText())
                .resolveVariable(LEVEL, readingExerciseModel.getLevel())
                .resolveVariable(TOPIC, readingExerciseModel.getTopic());

        var generatedText = aiGateway.sendRequest(generateText.getPrompt(), ReadingExercise.TextOutput.class);
        outputGateway.sendPlainMessage(parameters.chatId(), "The text is generated.");
        return generatedText;
    }

    private ReadingExercise.ReadingTasks generateTasks(CommandParameters parameters, ReadingExercise.TextOutput generatedText) {
        outputGateway.sendPlainMessage(parameters.chatId(), "Creating reading exercise...");
        GenericPromptTemplate createExercise = new GenericPromptTemplate(readingPromptsConfiguration.createReadingExercise())
                .resolveVariable(TEXT, generatedText.text());
        var tasks = aiGateway.sendRequest(createExercise.getPrompt(), ReadingExercise.ReadingTasks.class);
        outputGateway.sendPlainMessage(parameters.chatId(), "The exercise is generated.");
        return tasks;
    }

    private ReadingExercise.Paragraphs mapToParagraphs(CommandParameters parameters, ReadingExercise.TextOutput generatedText) {
        outputGateway.sendPlainMessage(parameters.chatId(), "Splitting the text into paragraphs....");
        GenericPromptTemplate textToParagraphsPrompt = new GenericPromptTemplate(readingPromptsConfiguration.textToParagraphs())
                .resolveVariable(TEXT, generatedText.text());
        return aiGateway.sendRequest(textToParagraphsPrompt.getPrompt(), ReadingExercise.Paragraphs.class);
    }

    private ReadingExercise.Document generateDocument(CommandParameters parameters,
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

    private Images pipeDocument(CommandParameters parameters, ReadingExercise.Document documentObject) {
        outputGateway.sendPlainMessage(parameters.chatId(), "Piping the document into media files...");
        return readingExercisePipe.pipe(documentObject);
    }

    @Override
    public AbstractCommandModel<?> supportedCommandModel() {
        return new ReadingExerciseModel()
                .init()
                .addMapping(LEVEL, ReadingExerciseModel::setLevel)
                .addMapping(TOPIC, ReadingExerciseModel::setTopic);
    }
}
