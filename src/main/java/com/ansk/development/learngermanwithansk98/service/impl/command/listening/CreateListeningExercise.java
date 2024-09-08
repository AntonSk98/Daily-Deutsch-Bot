package com.ansk.development.learngermanwithansk98.service.impl.command.listening;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.config.ListeningPromptConfiguration;
import com.ansk.development.learngermanwithansk98.gateway.OutputGateway;
import com.ansk.development.learngermanwithansk98.gateway.openai.OpenAiGateway;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.service.impl.command.AbstractCommandService;
import com.ansk.development.learngermanwithansk98.service.impl.pipe.ListeningExerciseDocumentPipe;
import com.ansk.development.learngermanwithansk98.service.model.Command;
import com.ansk.development.learngermanwithansk98.service.model.GenericPromptTemplate;
import com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel;
import com.ansk.development.learngermanwithansk98.service.model.input.CommandParameters;
import com.ansk.development.learngermanwithansk98.service.model.input.ListeningExerciseModel;
import com.ansk.development.learngermanwithansk98.service.model.output.ExerciseDocument;
import com.ansk.development.learngermanwithansk98.service.model.output.ListeningExercise;
import org.springframework.stereotype.Service;

import java.io.InputStream;

import static com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel.Properties.AUDIO;
import static com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel.Properties.TEXT;

@Service
public class CreateListeningExercise extends AbstractCommandService {

    private final OutputGateway outputGateway;
    private final OpenAiGateway aiGateway;
    private final ListeningPromptConfiguration promptsConfiguration;
    private final ListeningExerciseDocumentPipe documentPipe;

    protected CreateListeningExercise(CommandsConfiguration commandsConfiguration,
                                      OutputGateway outputGateway,
                                      CommandCache commandCache,
                                      OpenAiGateway aiGateway,
                                      ListeningPromptConfiguration promptConfiguration,
                                      ListeningExerciseDocumentPipe documentPipe) {
        super(commandsConfiguration, outputGateway, commandCache);
        this.outputGateway = outputGateway;
        this.aiGateway = aiGateway;
        this.promptsConfiguration = promptConfiguration;
        this.documentPipe = documentPipe;
    }

    @Override
    public Command supportedCommand() {
        return Command.LISTENING_EXERCISE;
    }

    @Override
    public void applyCommandModel(AbstractCommandModel<?> model, CommandParameters parameters) {
        ListeningExerciseModel listeningExerciseModel = model.map(ListeningExerciseModel.class);
        InputStream audioStream = outputGateway.audioStream(listeningExerciseModel.getAudio());
        String transcribedAudio = aiGateway.transcribeAudio(audioStream);

        GenericPromptTemplate transcribedAudioToParagraphs = new GenericPromptTemplate(promptsConfiguration.textToParagraphs())
                .resolveVariable(TEXT, transcribedAudio);

        var paragraphs = aiGateway.sendRequest(transcribedAudioToParagraphs.getPrompt(), ListeningExercise.Paragraphs.class);

        GenericPromptTemplate listeningExercisePrompt = new GenericPromptTemplate(promptsConfiguration.createListeningExercise())
                .resolveVariable(TEXT, transcribedAudio);

        var listeningExerciseOutput = aiGateway.sendRequest(listeningExercisePrompt.getPrompt(), ListeningExercise.Output.class);

        var listeningExerciseDocumentMetadata = new ListeningExercise.Document(
                listeningExerciseOutput.level(),
                listeningExerciseOutput.title(),
                paragraphs.paragraphs(),
                listeningExerciseOutput.tasks().stream().map(ListeningExercise.Task::question).toList()
        );


        ExerciseDocument listeningExerciseDocument = documentPipe.pipe(listeningExerciseDocumentMetadata);

        ListeningExercise listeningExercise = new ListeningExercise(
                listeningExerciseModel.getAudio(),
                new ListeningExercise.ListeningTasks(listeningExerciseOutput.tasks()),
                listeningExerciseDocument
        );


        outputGateway.sendListeningExercise(parameters.chatId(), listeningExercise);
    }

    @Override
    public AbstractCommandModel<?> supportedCommandModel() {
        return new ListeningExerciseModel().init().addMapping(AUDIO, ListeningExerciseModel::setAudio);
    }
}
