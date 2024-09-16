package com.ansk.development.learngermanwithansk98.gateway.telegram.integration;

import com.ansk.development.learngermanwithansk98.service.model.output.EditListeningExercisePrompt;
import com.ansk.development.learngermanwithansk98.service.model.output.ListeningExercise;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.ansk.development.learngermanwithansk98.gateway.telegram.integration.TelegramSenderSupport.documentSender;
import static com.ansk.development.learngermanwithansk98.gateway.telegram.integration.TelegramSenderSupport.questionsAndAnswersMessageBlock;

/**
 * Sends listening exercises with audio and documents to Telegram.
 *
 * @author Anton Skripin
 */
public class AudioExerciseSender {

    private static final String LISTENING_EXERCISE_TEMPLATE = """
            ⭐️ #Listening <b>| %s</b>
            
            🎧 Listen to the audio and complete the exercise in the 🗒️ document.
            
            🔹🔹🔹
            """;

    private static final String LISTENING_EXERCISE_DOCUMENT = """
            📄️ #ListeningExercise
            
            ✅ When you're done, feel free to check the answers below ⬇️
            """;

    private final TelegramClient telegramClient;
    private final String token;

    /**
     * Constructor.
     *
     * @param telegramClient See {@link TelegramClient}
     * @param token          bot token
     */
    public AudioExerciseSender(TelegramClient telegramClient, String token) {
        this.telegramClient = telegramClient;
        this.token = token;
    }

    /**
     * Streams an audio from Telegram by its audio id.
     *
     * @param audioId audio id
     * @return streaming audio
     */
    public InputStream streamAudio(String audioId) {
        GetFile audioMetadata = new GetFile(audioId);
        try {
            File audioFile = telegramClient.execute(audioMetadata);
            return new URI(audioFile.getFileUrl(token)).toURL().openStream();
        } catch (TelegramApiException | IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends the listening exercise audio, document, and task details to the specified chat.
     *
     * @param chatId            the Telegram chat ID to send the exercise to
     * @param listeningExercise the listening exercise containing audio and tasks
     */
    public void sendListeningExercise(Long chatId, ListeningExercise listeningExercise) {
        InputStream audioStream = streamAudio(listeningExercise.audio());
        InputFile audioFile = new InputFile(audioStream, "audio.mp3");
        SendAudio sendAudio = SendAudio.builder()
                .chatId(chatId)
                .audio(audioFile)
                .caption(String.format(LISTENING_EXERCISE_TEMPLATE, listeningExercise.level()))
                .parseMode("HTML")
                .build();


        var audioDocumentCaption = new TelegramSenderSupport.DocumentCaption(LISTENING_EXERCISE_DOCUMENT, "HTML");
        var renderingParams = new TelegramSenderSupport.DocumentRenderingParams(Optional.of(audioDocumentCaption), false);
        Consumer<TelegramClient> audioExerciseSender = documentSender(chatId, listeningExercise.document(), renderingParams);

        String exercisePart = questionsAndAnswersMessageBlock(listeningExercise.tasks()
                .tasks()
                .stream()
                .collect(Collectors.toMap(ListeningExercise.Task::question, ListeningExercise.Task::answer))
        );

        SendMessage questionsAndAnswers = SendMessage.builder().chatId(chatId).text(exercisePart).parseMode("HTML").build();
        try {
            telegramClient.execute(sendAudio);
            audioExerciseSender.accept(telegramClient);
            telegramClient.execute(questionsAndAnswers);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sends a dynamic prompt to edit listening exercise.
     *
     * @param chatId                      chat id
     * @param editListeningExercisePrompt {@link EditListeningExercisePrompt}
     */
    public void sendPromptToEditListeningExercise(Long chatId, EditListeningExercisePrompt editListeningExercisePrompt) {
        SendAudio sendAudio = SendAudio.builder()
                .chatId(chatId)
                .audio(new InputFile(streamAudio(editListeningExercisePrompt.audio()), "tmp.mp3"))
                .build();
        SendMessage text = SendMessage.builder().chatId(chatId).text(editListeningExercisePrompt.transcription()).build();

        try {
            telegramClient.execute(sendAudio);
            telegramClient.execute(text);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
