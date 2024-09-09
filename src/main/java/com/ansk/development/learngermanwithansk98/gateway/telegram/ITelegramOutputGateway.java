package com.ansk.development.learngermanwithansk98.gateway.telegram;

import com.ansk.development.learngermanwithansk98.service.model.output.ExerciseDocument;
import com.ansk.development.learngermanwithansk98.service.model.output.ListeningExercise;
import com.ansk.development.learngermanwithansk98.service.model.output.ReadingExercise;
import com.ansk.development.learngermanwithansk98.service.model.output.WritingExercise;
import okhttp3.OkHttpClient;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

/**
 * Defines operations for sending various types of messages and exercises via Telegram.
 *
 * @author Anton Skripin
 */
public interface ITelegramOutputGateway {

    /**
     * Sends a plain text message to the specified chat.
     *
     * @param chatId  the ID of the chat
     * @param message the message content
     */
    void sendPlainMessage(Long chatId, String message);

    /**
     * Sends a message with navigation buttons to the specified chat.
     *
     * @param chatId  the ID of the chat
     * @param message the message content
     */
    void sendMessageWithNavigation(Long chatId, String message);

    /**
     * Sends a message with an attached code payload to the specified chat.
     *
     * @param chatId  the ID of the chat
     * @param message the message content
     * @param <T>     the type of the payload
     * @param payload the payload data
     */
    <T> void sendMessageWithPayload(Long chatId, String message, T payload);

    /**
     * Sends a word card exercise to the specified chat.
     *
     * @param chatId           the ID of the chat
     * @param exerciseDocument the exercise document
     */
    void sendWordCard(Long chatId, ExerciseDocument exerciseDocument);

    /**
     * Sends a reading exercise to the specified chat.
     *
     * @param chatId          the ID of the chat
     * @param readingExercise the reading exercise content
     */
    void sendReadingExercise(Long chatId, ReadingExercise readingExercise);

    /**
     * Sends a writing exercise to the specified chat.
     *
     * @param chatId          the ID of the chat
     * @param writingExercise writing exercise content
     */
    void sendWritingExercise(Long chatId, WritingExercise writingExercise);

    /**
     * Sends a listening exercise to the specified chat.
     *
     * @param chatId            the ID of the chat
     * @param listeningExercise the listening exercise content
     */
    void sendListeningExercise(Long chatId, ListeningExercise listeningExercise);

    /**
     * Streams audio from Telegram by its audio ID.
     *
     * @param audioId the ID of the audio file
     * @return an InputStream of the audio file
     */
    InputStream streamAudio(String audioId);

    /**
     * Creates a default {@link TelegramClient}.
     *
     * @param token bot token
     * @return telegram client
     */
    default TelegramClient createClient(String token) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

        return new OkHttpTelegramClient(client, token);
    }
}
