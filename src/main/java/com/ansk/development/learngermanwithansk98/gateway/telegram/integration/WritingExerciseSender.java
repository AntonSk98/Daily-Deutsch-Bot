package com.ansk.development.learngermanwithansk98.gateway.telegram.integration;

import com.ansk.development.learngermanwithansk98.service.model.output.ExerciseDocument;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Optional;

import static com.ansk.development.learngermanwithansk98.gateway.telegram.integration.TelegramSenderSupport.documentSender;

/**
 * Component to send writing exercises.
 *
 * @author Anton Skripin
 */
public class WritingExerciseSender {

    private final TelegramClient telegramClient;

    private static final String WRITING_EXERCISE_TEMPLATE = """
            ⭐️ #Writing
            
            🗒️ Please write your opinion on the following topic:
            
            <b>%s</b>
            
            Also feel free to check out our sample text ⬆️
            """;

    /**
     * Constructor.
     *
     * @param telegramClient See {@link TelegramClient}
     */
    public WritingExerciseSender(TelegramClient telegramClient) {
        this.telegramClient = telegramClient;
    }

    /**
     * Sends a writing exercise
     *
     * @param chatId                  chat id
     * @param topic                   topic to write an essay about
     * @param writingExerciseDocument document with writing exercise and a sample essay
     */
    public void sendWritingExercise(Long chatId, String topic, ExerciseDocument writingExerciseDocument) {
        final String writingExercise = String.format(WRITING_EXERCISE_TEMPLATE, topic);

        var documentCaption = new TelegramSenderSupport.DocumentCaption(writingExercise, "HTML");
        var mediaParameters = new TelegramSenderSupport.DocumentRenderingParams(Optional.of(documentCaption), true);

        documentSender(chatId, writingExerciseDocument, mediaParameters).accept(telegramClient);
    }
}
