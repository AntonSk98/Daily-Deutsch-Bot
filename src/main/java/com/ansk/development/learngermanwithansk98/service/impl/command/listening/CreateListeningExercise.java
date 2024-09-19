package com.ansk.development.learngermanwithansk98.service.impl.command.listening;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.config.ListeningPromptConfiguration;
import com.ansk.development.learngermanwithansk98.gateway.openai.AIGateway;
import com.ansk.development.learngermanwithansk98.gateway.telegram.ITelegramOutputGateway;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.repository.ListeningExerciseCache;
import com.ansk.development.learngermanwithansk98.service.impl.command.AbstractCommandProcessor;
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
import java.util.Objects;

import static com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel.Properties.AUDIO;
import static com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel.Properties.TEXT;

/**
 * Service to create a listening exercise.
 *
 * @author Anton Skripin
 */
@Service
public class CreateListeningExercise extends AbstractCommandProcessor {

    private final ITelegramOutputGateway telegramOutputGateway;
    private final AIGateway aiGateway;
    private final ListeningPromptConfiguration promptsConfiguration;
    private final ListeningExerciseDocumentPipe documentPipe;
    private final ListeningExerciseCache listeningExerciseCache;

    /**
     * Constructor.
     *
     * @param commandsConfiguration  See {@link CommandsConfiguration}
     * @param telegramOutputGateway  See {@link ITelegramOutputGateway}
     * @param commandCache           See {@link CommandCache}
     * @param aiGateway              See {@link AIGateway}
     * @param promptConfiguration    See {@link ListeningPromptConfiguration}
     * @param documentPipe           See {@link ListeningExerciseDocumentPipe}
     * @param listeningExerciseCache See {@link ListeningExerciseCache}
     */
    protected CreateListeningExercise(CommandsConfiguration commandsConfiguration,
                                      ITelegramOutputGateway telegramOutputGateway,
                                      CommandCache commandCache,
                                      AIGateway aiGateway,
                                      ListeningPromptConfiguration promptConfiguration,
                                      ListeningExerciseDocumentPipe documentPipe,
                                      ListeningExerciseCache listeningExerciseCache) {
        super(commandsConfiguration, telegramOutputGateway, commandCache);
        this.telegramOutputGateway = telegramOutputGateway;
        this.aiGateway = aiGateway;
        this.promptsConfiguration = promptConfiguration;
        this.documentPipe = documentPipe;
        this.listeningExerciseCache = listeningExerciseCache;
    }

    @Override
    public Command supportedCommand() {
        return Command.LISTENING_EXERCISE;
    }

    @Override
    public void applyCommandModel(AbstractCommandModel<?> model, CommandParameters parameters) {
        telegramOutputGateway.sendPlainMessage(parameters.chatId(), "Transcribing an audio");
        ListeningExerciseModel listeningExerciseModel = model.map(ListeningExerciseModel.class);
        InputStream audioStream = telegramOutputGateway.streamAudio(listeningExerciseModel.getAudio());
        String transcribedAudio = aiGateway.transcribeAudio(audioStream);

        telegramOutputGateway.sendPlainMessage(parameters.chatId(), "Splitting transcribed text into paragraphs");
        GenericPromptTemplate transcribedAudioToParagraphs = new GenericPromptTemplate(promptsConfiguration.textToParagraphs())
                .resolveVariable(TEXT, transcribedAudio);

        telegramOutputGateway.sendPlainMessage(parameters.chatId(), "Creating listening exercise");
        var paragraphs = aiGateway.sendRequest(transcribedAudioToParagraphs.getPrompt(), ListeningExercise.Paragraphs.class);

        GenericPromptTemplate listeningExercisePrompt = new GenericPromptTemplate(promptsConfiguration.createListeningExercise())
                .resolveVariable(TEXT, transcribedAudio);

        var listeningExerciseOutput = aiGateway.sendRequest(listeningExercisePrompt.getPrompt(), ListeningExercise.Output.class);

        Objects.requireNonNull(listeningExerciseOutput.level(), "Listening exercise | Level cannot be null");
        Objects.requireNonNull(listeningExerciseOutput.title(), "Listening exercise | Title cannot be null");
        Objects.requireNonNull(paragraphs.paragraphs(), "Transcription cannot be null");
        Objects.requireNonNull(listeningExerciseOutput.tasks(), "Listening tasks cannot be null");

        var listeningExerciseDocumentMetadata = new ListeningExercise.Document(
                listeningExerciseOutput.level(),
                listeningExerciseOutput.title(),
                paragraphs.paragraphs(),
                listeningExerciseOutput.tasks().stream().map(ListeningExercise.Task::question).toList()
        );

        telegramOutputGateway.sendPlainMessage(parameters.chatId(), "Creating a document with listening exercise");
        ExerciseDocument listeningExerciseDocument = documentPipe.pipe(listeningExerciseDocumentMetadata);

        ListeningExercise listeningExercise = new ListeningExercise(
                listeningExerciseModel.getAudio(),
                listeningExerciseOutput.level(),
                listeningExerciseOutput.title(),
                new ListeningExercise.ListeningTasks(listeningExerciseOutput.tasks()),
                listeningExerciseDocument,
                paragraphs.paragraphs()
        );

        telegramOutputGateway.sendPlainMessage(parameters.chatId(), "Saving it all into cache");
        listeningExerciseCache.saveListeningExercise(listeningExercise);
        telegramOutputGateway.sendPlainMessage(parameters.chatId(), "Listening exercise saved into cache");
    }

    @Override
    public AbstractCommandModel<?> supportedModelWithMapping() {
        return new ListeningExerciseModel().init().addMapping(AUDIO, ListeningExerciseModel::setAudio);
    }
}
