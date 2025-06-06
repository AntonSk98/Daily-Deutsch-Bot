package com.ansk.development.learngermanwithansk98.service.impl.command.listening;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.config.ListeningPromptConfiguration;
import com.ansk.development.learngermanwithansk98.integration.openai.OpenAiClient;
import com.ansk.development.learngermanwithansk98.integration.telegram.ITelegramClient;
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

    private final ITelegramClient telegramClient;
    private final OpenAiClient openAiClient;
    private final ListeningPromptConfiguration promptsConfiguration;
    private final ListeningExerciseDocumentPipe documentPipe;
    private final ListeningExerciseCache listeningExerciseCache;

    /**
     * Constructor.
     *
     * @param commandsConfiguration  See {@link CommandsConfiguration}
     * @param telegramClient  See {@link ITelegramClient}
     * @param commandCache           See {@link CommandCache}
     * @param openAiClient              See {@link OpenAiClient}
     * @param promptConfiguration    See {@link ListeningPromptConfiguration}
     * @param documentPipe           See {@link ListeningExerciseDocumentPipe}
     * @param listeningExerciseCache See {@link ListeningExerciseCache}
     */
    protected CreateListeningExercise(CommandsConfiguration commandsConfiguration,
                                      ITelegramClient telegramClient,
                                      CommandCache commandCache,
                                      OpenAiClient openAiClient,
                                      ListeningPromptConfiguration promptConfiguration,
                                      ListeningExerciseDocumentPipe documentPipe,
                                      ListeningExerciseCache listeningExerciseCache) {
        super(commandsConfiguration, telegramClient, commandCache);
        this.telegramClient = telegramClient;
        this.openAiClient = openAiClient;
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
        telegramClient.sendPlainMessage(parameters.chatId(), "Transcribing an audio");
        ListeningExerciseModel listeningExerciseModel = model.map(ListeningExerciseModel.class);
        InputStream audioStream = telegramClient.streamAudio(listeningExerciseModel.getAudio());
        String transcribedAudio = openAiClient.transcribeAudio(audioStream);

        telegramClient.sendPlainMessage(parameters.chatId(), "Splitting transcribed text into paragraphs");
        GenericPromptTemplate transcribedAudioToParagraphs = new GenericPromptTemplate(promptsConfiguration.textToParagraphs())
                .resolveVariable(TEXT, transcribedAudio);

        telegramClient.sendPlainMessage(parameters.chatId(), "Creating listening exercise");
        var paragraphs = openAiClient.sendRequest(transcribedAudioToParagraphs.getPrompt(), ListeningExercise.Paragraphs.class);

        GenericPromptTemplate listeningExercisePrompt = new GenericPromptTemplate(promptsConfiguration.createListeningExercise())
                .resolveVariable(TEXT, transcribedAudio);

        var listeningExerciseOutput = openAiClient.sendRequest(listeningExercisePrompt.getPrompt(), ListeningExercise.Output.class);

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

        telegramClient.sendPlainMessage(parameters.chatId(), "Creating a document with listening exercise");
        ExerciseDocument listeningExerciseDocument = documentPipe.pipe(listeningExerciseDocumentMetadata);

        ListeningExercise listeningExercise = new ListeningExercise(
                listeningExerciseModel.getAudio(),
                listeningExerciseOutput.level(),
                listeningExerciseOutput.title(),
                new ListeningExercise.ListeningTasks(listeningExerciseOutput.tasks()),
                listeningExerciseDocument,
                paragraphs.paragraphs()
        );

        telegramClient.sendPlainMessage(parameters.chatId(), "Saving it all into cache");
        listeningExerciseCache.saveListeningExercise(listeningExercise);
        telegramClient.sendPlainMessage(parameters.chatId(), "Listening exercise saved into cache");
    }

    @Override
    public AbstractCommandModel<?> supportedModelWithMapping() {
        return new ListeningExerciseModel().init().addMapping(AUDIO, ListeningExerciseModel::setAudio);
    }
}
