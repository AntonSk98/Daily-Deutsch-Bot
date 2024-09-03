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
import com.ansk.development.learngermanwithansk98.service.model.input.ReadingExerciseWithTextModel;
import com.ansk.development.learngermanwithansk98.service.model.output.ReadingExercise;
import org.springframework.stereotype.Service;

import static com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel.Properties.TEXT;

@Service
public class CreateReadingExercise extends AbstractCommandService {

    private final OpenAiGateway aiGateway;
    private final OutputGateway outputGateway;
    private final ReadingPromptsConfiguration readingPromptsConfiguration;
    private final ReadingExerciseSupport readingExerciseSupport;

    protected CreateReadingExercise(CommandsConfiguration commandsConfiguration,
                                    OutputGateway outputGateway,
                                    CommandCache commandCache,
                                    OpenAiGateway aiGateway,
                                    ReadingExerciseDocumentPipe readingExercisePipe,
                                    ReadingPromptsConfiguration readingPromptsConfiguration) {
        super(commandsConfiguration, outputGateway, commandCache);
        this.outputGateway = outputGateway;
        this.aiGateway = aiGateway;
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
        return Command.READING_EXERCISE_CREATE;
    }

    @Override
    public void applyCommandModel(AbstractCommandModel<?> abstractModel, CommandParameters parameters) {
        ReadingExerciseWithTextModel model = abstractModel.map(ReadingExerciseWithTextModel.class);
        var analyzedText = analyzeText(parameters, model);
        var tasks = readingExerciseSupport.generateTasks(parameters, analyzedText);
        var paragraphs = readingExerciseSupport.mapToParagraphs(parameters, analyzedText);
        var documentObject = readingExerciseSupport.generateDocument(parameters, analyzedText, paragraphs, tasks);
        var document = readingExerciseSupport.pipeDocument(parameters, documentObject);

        var readingExercise = new ReadingExercise(
                analyzedText.title(),
                paragraphs,
                tasks,
                document
        );

        outputGateway.sendReadingExercise(parameters.chatId(), readingExercise);
    }

    private ReadingExercise.TextOutput analyzeText(CommandParameters parameters, ReadingExerciseWithTextModel model) {
        outputGateway.sendPlainMessage(parameters.chatId(), "Analyzing and rephrasing the text...");
        GenericPromptTemplate analyzeText = new GenericPromptTemplate(readingPromptsConfiguration.rephraseText())
                .resolveVariable(TEXT, model.getText());
        var analyzedText = aiGateway.sendRequest(analyzeText.getPrompt(), ReadingExercise.TextOutput.class);
        outputGateway.sendPlainMessage(parameters.chatId(), "The text is successfully analyzed.");
        return analyzedText;
    }

    @Override
    public AbstractCommandModel<?> supportedCommandModel() {
        return new ReadingExerciseWithTextModel()
                .init()
                .addMapping(TEXT, ReadingExerciseWithTextModel::setText);
    }
}
