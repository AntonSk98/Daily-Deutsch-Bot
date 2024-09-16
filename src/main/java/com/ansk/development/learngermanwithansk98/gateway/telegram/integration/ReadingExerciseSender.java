package com.ansk.development.learngermanwithansk98.gateway.telegram.integration;

import com.ansk.development.learngermanwithansk98.service.model.output.ReadingExercise;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static com.ansk.development.learngermanwithansk98.gateway.telegram.integration.TelegramSenderSupport.documentSender;
import static com.ansk.development.learngermanwithansk98.gateway.telegram.integration.TelegramSenderSupport.questionsAndAnswersMessageBlock;

/**
 * Component to send reading exercises.
 *
 * @author Anton Skripin
 */
public class ReadingExerciseSender {

    private static final String READING_EXERCISE_TEMPLATE = """
            ⭐️ #Reading <b> | %s</b>
            <i>Please read the text and complete the exercise below.</i>
            
            📚 <b>%s</b>
            
            <i>%s</i>
            
            🔹🔹🔹
            
            """;

    private static final String READING_EXERCISE_DOCUMENT = """
            📚️ #ReadingExercise
            
            ✅ When you're done, feel free to check the answers below ⬇️
            """;

    private final TelegramClient telegramClient;

    public ReadingExerciseSender(TelegramClient telegramClient) {
        this.telegramClient = telegramClient;
    }

    /**
     * Sends a reading exercise.
     *
     * @param chatId          chat id
     * @param readingExercise reading exercise
     */
    public void sendReadingExercise(Long chatId, ReadingExercise readingExercise) {
        TelegramSenderSupport.DocumentRenderingParams documentRenderingParams = documentRenderingParameters();
        SendMessage readingText = readingText(chatId, readingExercise);
        SendMessage questionsAndAnswers = questionsAndAnswers(chatId, readingExercise.tasks());
        Consumer<TelegramClient> readingExerciseDocumentSender = documentSender(chatId, readingExercise.document(), documentRenderingParams);
        try {
            telegramClient.execute(readingText);
            readingExerciseDocumentSender.accept(telegramClient);
            telegramClient.execute(questionsAndAnswers);
        } catch (TelegramApiException e) {
            throw new IllegalStateException("Error occurred while sending reading exercise", e);
        }
    }

    private TelegramSenderSupport.DocumentRenderingParams documentRenderingParameters() {
        var caption = new TelegramSenderSupport.DocumentCaption(READING_EXERCISE_DOCUMENT, "HTML");
        return new TelegramSenderSupport.DocumentRenderingParams(Optional.of(caption), false);
    }

    private SendMessage readingText(Long chatId, ReadingExercise readingExercise) {
        final String title = readingExercise.title();
        final String text = String.join("\n\n", readingExercise.paragraphs().paragraphs());
        final String readingText = String.format(READING_EXERCISE_TEMPLATE, readingExercise.level(), title, text);
        return SendMessage.builder().chatId(chatId).text(readingText).parseMode("HTML").build();

    }

    private SendMessage questionsAndAnswers(Long chatId, ReadingExercise.ReadingTasks tasks) {
        String questionsAndAnswers = questionsAndAnswersMessageBlock(tasks
                .tasks()
                .stream()
                .collect(Collectors.toMap(ReadingExercise.Task::question, ReadingExercise.Task::answer)));

        return SendMessage.builder().chatId(chatId).text(questionsAndAnswers).parseMode("HTML").build();
    }
}
