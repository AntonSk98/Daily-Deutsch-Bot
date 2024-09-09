package com.ansk.development.learngermanwithansk98.gateway.telegram.integration;

import com.ansk.development.learngermanwithansk98.service.model.output.ReadingExercise;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ansk.development.learngermanwithansk98.gateway.telegram.integration.TelegramSenderSupport.documentSender;
import static com.ansk.development.learngermanwithansk98.gateway.telegram.integration.TelegramSenderSupport.questionsAndAnswersMessageBlock;

/**
 * Component to send reading exercises.
 *
 * @author Anton Skripin
 */
public class ReadingExerciseSender {

    private static final int MAX_MESSAGE_LENGTH = 4096;

    private static final String READING_EXERCISE_TEMPLATE = """
            ⭐️ #Reading
            
            📚 <b>%s</b>
            
            <i>%s</i>
            """;

    private static final String READING_EXERCISE_DOCUMENT = """
            📄️ #ReadingExerciseDocument
            📚 Here’s the reading exercise.
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
        List<SendMessage> readingExerciseMessages = toReadingExerciseMessage(chatId, readingExercise);
        Consumer<TelegramClient> readingExerciseDocumentSender = documentSender(chatId, readingExercise.document(), documentRenderingParams);
        try {
            for (SendMessage readingExerciseMessage : readingExerciseMessages) {
                telegramClient.execute(readingExerciseMessage);
            }
            readingExerciseDocumentSender.accept(telegramClient);
        } catch (TelegramApiException e) {
            throw new IllegalStateException("Error occurred while sending reading exercise", e);
        }
    }

    private TelegramSenderSupport.DocumentRenderingParams documentRenderingParameters() {
        var caption = new TelegramSenderSupport.DocumentCaption(READING_EXERCISE_DOCUMENT, "HTML");
        return new TelegramSenderSupport.DocumentRenderingParams(Optional.of(caption), false);
    }

    private List<SendMessage> toReadingExerciseMessage(Long chatId, ReadingExercise readingExercise) {
        final String title = readingExercise.title();
        final String text = String.join("\n\n", readingExercise.paragraphs().paragraphs());
        final String readingText = String.format(READING_EXERCISE_TEMPLATE, title, text);
        return toExerciseWithQuestionAnswers(
                chatId,
                readingText,
                readingExercise.tasks()
                        .tasks()
                        .stream()
                        .collect(Collectors.toMap(ReadingExercise.Task::question, ReadingExercise.Task::answer))
        );
    }

    private List<SendMessage> toExerciseWithQuestionAnswers(Long chatId, String payload, Map<String, String> questionAndAnswerMap) {
        String exercisePart = questionsAndAnswersMessageBlock(questionAndAnswerMap);
        String payloadAndExercisePart = String.join("\n", payload, exercisePart);

        if (payloadAndExercisePart.getBytes().length >= MAX_MESSAGE_LENGTH) {
            Function<String, SendMessage> toSendMessage = txt -> SendMessage.builder().chatId(chatId).text(txt).parseMode("HTML").build();
            return Stream.of(payload, exercisePart).map(toSendMessage).toList();
        }

        return Collections.singletonList(SendMessage.builder().chatId(chatId).text(payloadAndExercisePart).parseMode("HTML").build());
    }


}
