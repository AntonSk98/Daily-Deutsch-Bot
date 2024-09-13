package com.ansk.development.learngermanwithansk98.gateway.telegram.integration;

import com.ansk.development.learngermanwithansk98.service.model.output.WritingExercise;
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
            
            🗒️ Share your thoughts on the topic below:
            
            <b>%s</b>
            
            You can also always review our sample text for inspiration ⬆️
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
     * @param chatId          chat id
     * @param writingExercise writing exercise
     */
    public void sendWritingExercise(Long chatId, WritingExercise writingExercise) {
        final String writingExerciseText = String.format(WRITING_EXERCISE_TEMPLATE, writingExercise.topic());

        var documentCaption = new TelegramSenderSupport.DocumentCaption(writingExerciseText, "HTML");
        var mediaParameters = new TelegramSenderSupport.DocumentRenderingParams(Optional.of(documentCaption), true);

        documentSender(chatId, writingExercise.exerciseDocument(), mediaParameters).accept(telegramClient);
    }
}
