package com.ansk.development.learngermanwithansk98.gateway;

import com.ansk.development.learngermanwithansk98.config.DailyDeutschBotConfiguration;
import com.ansk.development.learngermanwithansk98.service.model.output.Images;
import com.ansk.development.learngermanwithansk98.service.model.output.ReadingExercise;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMedia;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ansk.development.learngermanwithansk98.service.model.Navigation.NEXT;
import static com.ansk.development.learngermanwithansk98.service.model.Navigation.PREVIOUS;

/**
 * A component responsible for sending messages.
 *
 * @author Anton Skripin
 */
@Component
public class OutputGateway {

    private static final int MAX_MESSAGE_LENGTH = 4096;

    private final TelegramClient telegramClient;
    private final ObjectMapper objectMapper;

    public OutputGateway(DailyDeutschBotConfiguration config,
                         ObjectMapper objectMapper) {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

        this.telegramClient = new OkHttpTelegramClient(client, config.token());
        this.objectMapper = objectMapper;
    }

    public void sendPlainMessage(Long chatId, String message) {
        try {
            this.telegramClient.execute(SendMessage.builder().chatId(chatId).text(message).build());
        } catch (TelegramApiException e) {
            throw new IllegalStateException("Unexpected error occurred while sending a message to Telegram", e);
        }
    }

    public void sendMessageWithNavigation(Long chatId, String message) {
        InlineKeyboardMarkup keyboardMarkup = InlineKeyboardMarkup
                .builder()
                .keyboardRow(
                        new InlineKeyboardRow(
                                InlineKeyboardButton.builder().text(PREVIOUS.getText()).callbackData(PREVIOUS.getCommand()).build(),
                                InlineKeyboardButton.builder().text(NEXT.getText()).callbackData(NEXT.getCommand()).build()
                        ))
                .build();

        try {
            this.telegramClient.execute(SendMessage.builder().chatId(chatId).text(message).replyMarkup(keyboardMarkup).build());
        } catch (TelegramApiException e) {
            throw new IllegalStateException("Unexpected error occurred while sending a message with navigation to telegram", e);
        }

    }

    public <T> void sendMessageWithPayload(Long chatId, String message, T payload) {
        try {
            String json = objectMapper.copy().enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(payload);
            String output = String.format("""
                    %s
                    <pre><code>%s</code></pre>
                    """, message, json);
            SendMessage sendMessage = SendMessage.builder().parseMode("HTML")
                    .chatId(chatId)
                    .text(output)
                    .build();
            telegramClient.execute(sendMessage);
        } catch (TelegramApiException | JsonProcessingException e) {
            throw new IllegalStateException("Unexpected error occurred while sending a message with payload to telegram", e);
        }
    }

    public void sendHtmlMessage(Long chatId, String message) {
        SendMessage sendMessage = SendMessage.builder().chatId(chatId).text(message).parseMode("HTML").build();
        try {
            telegramClient.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new IllegalStateException("Error occurred while sending an HTML message to telegram", e);
        }
    }


    public void sendWordCard(Long chatId, Images images) {
        if (CollectionUtils.isEmpty(images.getBinaryImages())) {
            throw new IllegalStateException("No images passed as a parameter!");
        }

        try {
            if (images.onePage()) {
                SendPhoto mediaPhoto = toInputMediaPhoto(chatId, images.getBinaryImages().getFirst(), Optional.empty());
                telegramClient.execute(mediaPhoto);
                return;
            }

            var mediaPhotoGroup = toInputMediaPhotos(chatId, images.getBinaryImages(), Optional.empty());
            telegramClient.execute(mediaPhotoGroup);
        } catch (TelegramApiException e) {
            throw new IllegalStateException("Failed to send images to the client", e);
        }
    }

    public void sendReadingExercise(Long chatId, ReadingExercise readingExercise) {
        List<SendMessage> readingExerciseMessages = toReadingExercise(chatId, readingExercise);
        Runnable sendMediaGroup = sendMediaGroup(chatId, readingExercise);
        try {
            for (SendMessage readingExerciseMessage : readingExerciseMessages) {
                telegramClient.execute(readingExerciseMessage);
            }
            sendMediaGroup.run();
        } catch (TelegramApiException e) {
            throw new IllegalStateException("Error occurred while sending reading exercise", e);
        }
    }

    private Runnable sendMediaGroup(Long chatId, ReadingExercise readingExercise) {
        final String documentCaption = """
                🖨️ #ReadingForPrinting
                📚 Here’s the reading exercise formatted for printing.
                """;

        var caption = new ImageCaption(documentCaption, "HTML");

        return () -> {
            var document = readingExercise.document().onePage()
                    ? toInputMediaPhoto(chatId, readingExercise.document().getBinaryImages().getFirst(), Optional.of(caption))
                    : null;
            var documents = Objects.isNull(document)
                    ? toInputMediaPhotos(chatId, readingExercise.document().getBinaryImages(), Optional.of(caption))
                    : null;

            try {
                if (Objects.nonNull(document)) {
                    telegramClient.execute(document);
                    return;
                }

                if (Objects.nonNull(documents)) {
                    telegramClient.execute(documents);
                    return;
                }

                throw new IllegalStateException("Unknown state...");
            } catch (TelegramApiException e) {
                throw new IllegalStateException("Error occurred while sending reading exercise", e);
            }

        };
    }

    private Optional<SendPhoto> sendPhoto(Long chatId, ReadingExercise readingExercise) {
        return null;
    }

    private List<SendMessage> toReadingExercise(Long chatId, ReadingExercise readingExercise) {
        final String textTemplate = """
                ⭐️ #Reading
                
                📚 <b>%s</b>
                
                <i>%s</i>
                """;

        final String readingExerciseTemplate = """
                💬 <b>Questions and answers:</b>
                <blockquote expandable><span class="tg-spoiler">%s</span></blockquote>
                """;

        final String questionsAndAnswersTemplate = """
                <b>%s</b>
                <i>%s</i>
                """;

        final AtomicInteger counter = new AtomicInteger(1);
        final String title = readingExercise.title();
        final String text = String.join("\n\n", readingExercise.paragraphs().paragraphs());
        final String answers = readingExercise.tasks()
                .tasks()
                .stream()
                .map(task -> String.format(questionsAndAnswersTemplate, counter.getAndIncrement() + ". " + task.question(), task.answer()))
                .collect(Collectors.joining("\n"));

        final String readingText = String.format(textTemplate, title, text);
        final String exercisePart = String.format(readingExerciseTemplate, answers);
        final String readingExerciseMessage = String.join("\n", readingText, exercisePart);

        if (readingExerciseMessage.getBytes().length >= MAX_MESSAGE_LENGTH) {
            Function<String, SendMessage> toSendMessage = txt -> SendMessage.builder().chatId(chatId).text(txt).parseMode("HTML").build();
            return Stream.of(readingText, exercisePart).map(toSendMessage).toList();
        }

        return Collections.singletonList(SendMessage.builder().chatId(chatId).text(readingExerciseMessage).parseMode("HTML").build());
    }

    private SendMediaGroup toInputMediaPhotos(Long chatId, List<byte[]> binaryImages, Optional<ImageCaption> caption) {
        var builder = SendMediaGroup.builder().chatId(chatId);
        int index = 0;
        for (byte[] imageData : binaryImages) {
            InputMedia inputMedia = new InputMediaPhoto(new ByteArrayInputStream(imageData), "" + index);
            builder.media(inputMedia);
            if (index == 0 && caption.isPresent()) {
                inputMedia.setCaption(caption.get().caption);
                inputMedia.setParseMode(caption.get().parseMode);
            }
            index++;
        }
        return builder.build();
    }

    private SendPhoto toInputMediaPhoto(Long chatId, byte[] binaryImage, Optional<ImageCaption> caption) {
        return SendPhoto.builder()
                .chatId(chatId)
                .photo(new InputFile(new ByteArrayInputStream(binaryImage), "___.png"))
                .caption(caption.map(ImageCaption::caption).orElse(null))
                .parseMode(caption.map(ImageCaption::parseMode).orElse(null))
                .build();
    }

    private record ImageCaption(String caption, String parseMode) {
    }
}
