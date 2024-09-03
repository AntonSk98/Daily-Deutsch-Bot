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
public class GenerateReadingExerciseCommand extends AbstractCommandService {


    private final OpenAiGateway aiGateway;
    private final OutputGateway outputGateway;
    private final ReadingPromptsConfiguration readingPromptsConfiguration;
    private final ReadingExerciseSupport readingExerciseSupport;

    protected GenerateReadingExerciseCommand(CommandsConfiguration commandsConfiguration,
                                             OutputGateway outputGateway,
                                             OpenAiGateway aiGateway,
                                             CommandCache commandCache,
                                             ReadingPromptsConfiguration readingPromptsConfiguration,
                                             ReadingExerciseDocumentPipe readingExercisePipe) {
        super(commandsConfiguration, outputGateway, commandCache);
        this.aiGateway = aiGateway;
        this.outputGateway = outputGateway;
        this.readingPromptsConfiguration = readingPromptsConfiguration;
        this.readingExerciseSupport = new ReadingExerciseSupport(
                outputGateway,
                aiGateway,
                readingExercisePipe,
                readingPromptsConfiguration
        );
    }

    @Override
    public Command supportedCommand() {
        return Command.READING_EXERCISE_GENERATE;
    }

    @Override
    public void applyCommandModel(AbstractCommandModel<?> model, CommandParameters parameters) {
        ReadingExerciseModel readingExerciseModel = model.map(ReadingExerciseModel.class);
        var generatedText = generateText(parameters, readingExerciseModel);
        var tasks = readingExerciseSupport.generateTasks(parameters, generatedText);
        var paragraphs = readingExerciseSupport.mapToParagraphs(parameters, generatedText);
        var documentObject = readingExerciseSupport.generateDocument(parameters, generatedText, paragraphs, tasks);
        var document = readingExerciseSupport.pipeDocument(parameters, documentObject);

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

    @Override
    public AbstractCommandModel<?> supportedCommandModel() {
        return new ReadingExerciseModel()
                .init()
                .addMapping(LEVEL, ReadingExerciseModel::setLevel)
                .addMapping(TOPIC, ReadingExerciseModel::setTopic);
    }
}
