package com.ansk.development.learngermanwithansk98.integration.telegram.sender;

import com.ansk.development.learngermanwithansk98.service.model.output.ExerciseDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * Contains utility methods to send messages to Telegram
 *
 * @author Anton Skripin
 */
public class TelegramSenderSupport {

    private static final String EXERCISE_KEYS_TEMPLATE = """
            💬 💬 <b>Questions and answers:</b> 🤫 🤫
                <blockquote expandable><span class="tg-spoiler">%s</span></blockquote>
            """;

    private static final String QUESTIONS_AND_ANSWERS_TEMPLATE = """
            <b>%s</b>
            <i>%s</i>
            """;

    /**
     * Consumer that accepts {@link ExerciseDocument} and its {@link DocumentRenderingParams} and provides a consumer that sends the document.
     *
     * @param chatId                  chat id
     * @param exerciseDocument        exercise document
     * @param documentRenderingParams document rendering parameters
     * @return consumer that sends an exercise document
     */
    static Consumer<TelegramClient> documentSender(Long chatId, ExerciseDocument exerciseDocument, DocumentRenderingParams documentRenderingParams) {
        return telegramClient -> {
            try {
                if (exerciseDocument.onePage()) {
                    var onePageDocument = onePageDocument(chatId, exerciseDocument.pages().getFirst(), documentRenderingParams);
                    telegramClient.execute(onePageDocument);
                    return;
                }

                var document = severalPagesDocuments(chatId, exerciseDocument.pages(), documentRenderingParams);
                telegramClient.execute(document);
            } catch (TelegramApiException e) {
                throw new IllegalStateException("Error occurred while sending reading exercise", e);
            }

        };
    }

    /**
     * Returns a formatted message with questions and answers to them that are blurred by default.
     *
     * @param questionAndAnswerMap map where a key is a question and a value is an answer
     * @return formatted message with questions and answers
     */
    static String questionsAndAnswersMessageBlock(Map<String, String> questionAndAnswerMap) {
        final AtomicInteger counter = new AtomicInteger(1);
        final String questionAndAnswers = questionAndAnswerMap.entrySet()
                .stream()
                .map(questionAndAnswer -> String.format(
                                QUESTIONS_AND_ANSWERS_TEMPLATE,
                                counter.getAndIncrement() + ". " + questionAndAnswer.getKey(),
                                questionAndAnswer.getValue()
                        )
                )
                .collect(Collectors.joining("\n"));

        return String.format(EXERCISE_KEYS_TEMPLATE, questionAndAnswers);
    }

    /**
     * Consumer that sends an audio.
     *
     * @param chatId      chat id
     * @param audioStream audio stream
     * @param audioName   audio name with extension
     * @param caption     caption
     * @return consumer that sends an audio
     */
    static Consumer<TelegramClient> audioSender(Long chatId, InputStream audioStream, String audioName, String caption) {
        return telegramClient -> {
            InputFile audioFile = new InputFile(audioStream, audioName);

            SendAudio sendAudio = SendAudio.builder()
                    .chatId(chatId)
                    .audio(audioFile)
                    .caption(caption)
                    .parseMode("HTML")
                    .build();

            try {
                telegramClient.execute(sendAudio);
            } catch (TelegramApiException e) {
                throw new RuntimeException("Error occurred while sending audio exercise.", e);
            }
        };
    }

    private static SendMediaGroup severalPagesDocuments(Long chatId, List<byte[]> binaryImages, DocumentRenderingParams documentRenderingParams) {
        var builder = SendMediaGroup.builder().chatId(chatId);
        int index = 0;
        for (byte[] imageData : binaryImages) {
            InputMediaPhoto inputMedia = new InputMediaPhoto(new ByteArrayInputStream(imageData), "" + index);
            inputMedia.setHasSpoiler(documentRenderingParams.withSpoiler());
            builder.media(inputMedia);
            if (index == 0 && documentRenderingParams.imageCaption().isPresent()) {
                inputMedia.setCaption(documentRenderingParams.imageCaption().get().caption);
                inputMedia.setParseMode(documentRenderingParams.imageCaption().get().parseMode);
            }
            index++;
        }
        return builder.build();
    }

    private static SendPhoto onePageDocument(Long chatId, byte[] binaryImage, DocumentRenderingParams documentRenderingParams) {
        return SendPhoto.builder()
                .chatId(chatId)
                .photo(new InputFile(new ByteArrayInputStream(binaryImage), "___.png"))
                .caption(documentRenderingParams.imageCaption().map(DocumentCaption::caption).orElse(null))
                .hasSpoiler(documentRenderingParams.withSpoiler())
                .parseMode(documentRenderingParams.imageCaption().map(DocumentCaption::parseMode).orElse(null))
                .build();
    }

    record DocumentCaption(String caption, String parseMode) {
    }

    record DocumentRenderingParams(Optional<DocumentCaption> imageCaption, boolean withSpoiler) {
        static final DocumentRenderingParams NO_RENDERING_PARAMS = new DocumentRenderingParams(Optional.empty(), false);
    }
}
