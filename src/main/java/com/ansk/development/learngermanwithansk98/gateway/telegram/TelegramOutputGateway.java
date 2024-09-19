package com.ansk.development.learngermanwithansk98.gateway.telegram;

import com.ansk.development.learngermanwithansk98.config.DailyDeutschBotConfiguration;
import com.ansk.development.learngermanwithansk98.gateway.telegram.integration.*;
import com.ansk.development.learngermanwithansk98.service.model.output.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * Implementation of {@link ITelegramOutputGateway}.
 *
 * @author Anton Skripin
 */
@Component
public class TelegramOutputGateway implements ITelegramOutputGateway {

    private final MessageSender messageSender;
    private final WordCardSender wordCardSender;
    private final ReadingExerciseSender readingExerciseSender;
    private final WritingExerciseSender writingExerciseSender;
    private final AudioExerciseSender audioExerciseSender;

    /**
     * Constructor.
     *
     * @param config       See {@link DailyDeutschBotConfiguration}
     * @param objectMapper See {@link ObjectMapper}
     */
    public TelegramOutputGateway(DailyDeutschBotConfiguration config,
                                 ObjectMapper objectMapper) {
        var telegramClient = createClient(config.token());
        this.messageSender = new MessageSender(telegramClient, objectMapper);
        this.wordCardSender = new WordCardSender(telegramClient);
        this.readingExerciseSender = new ReadingExerciseSender(telegramClient);
        this.writingExerciseSender = new WritingExerciseSender(telegramClient);
        this.audioExerciseSender = new AudioExerciseSender(telegramClient, config.token());
    }

    @Override
    public void sendPlainMessage(Long chatId, String message) {
        messageSender.sendPlainMessage(chatId, message);
    }

    @Override
    public void sendMessageWithNavigation(Long chatId, String message) {
        messageSender.sendMessageWithNavigation(chatId, message);
    }

    @Override
    public void sendErrorMessage(Long chatId, Class<?> clazz, String message) {
        messageSender.sendErrorMessage(chatId, clazz, message);
    }

    @Override
    public <T> void sendMessageWithPayload(Long chatId, String message, T payload) {
        messageSender.sendMessageWithPayload(chatId, message, payload);
    }

    @Override
    public void sendWordCard(Long chatId, ExerciseDocument exerciseDocument) {
        wordCardSender.sendWordCard(chatId, exerciseDocument);
    }

    @Override
    public void sendReadingExercise(Long chatId, ReadingExercise readingExercise) {
        readingExerciseSender.sendReadingExercise(chatId, readingExercise);
    }

    @Override
    public void sendWritingExercise(Long chatId, WritingExercise writingExercise) {
        writingExerciseSender.sendWritingExercise(chatId, writingExercise);
    }

    @Override
    public InputStream streamAudio(String audioId) {
        return audioExerciseSender.streamAudio(audioId);
    }

    @Override
    public void sendListeningExercise(Long chatId, ListeningExercise listeningExercise) {
        audioExerciseSender.sendListeningExercise(chatId, listeningExercise);
    }

    @Override
    public void sendPromptToEditListeningExercise(Long chatId, EditListeningExercisePrompt dynamicPrompt) {
        audioExerciseSender.sendPromptToEditListeningExercise(chatId, dynamicPrompt);
    }

    @Override
    public void sendCorrectedText(Long chatId, ExerciseDocument originalTextDocument, ExerciseDocument correctedTextDocument) {
        writingExerciseSender.sendCorrectedText(chatId, originalTextDocument, correctedTextDocument);
    }


}
