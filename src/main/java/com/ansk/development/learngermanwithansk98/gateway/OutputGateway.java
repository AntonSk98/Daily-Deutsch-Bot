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
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.ansk.development.learngermanwithansk98.service.model.Navigation.NEXT;
import static com.ansk.development.learngermanwithansk98.service.model.Navigation.PREVIOUS;

/**
 * A component responsible for sending messages.
 *
 * @author Anton Skripin
 */
@Component
public class OutputGateway {

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
                SendPhoto mediaPhoto = toInputMediaPhoto(chatId, images.getBinaryImages().getFirst());
                telegramClient.execute(mediaPhoto);
                return;
            }

            var mediaPhotoGroup = toInputMediaPhotos(chatId, images.getBinaryImages());
            telegramClient.execute(mediaPhotoGroup);
        } catch (TelegramApiException e) {
            throw new IllegalStateException("Failed to send images to the client", e);
        }
    }

    public void sendReadingExercise(Long chatId, ReadingExercise readingExercise) {
        final String readingExerciseTemplate = """
                <b>%s</b>
                <i>%s</i>
                \n
                <b>Questions and answers:</b>
                <blockquote expandable><span class="tg-spoiler">%s</span></blockquote>
                """;

        final String answersTemplate = """
                <b>%s</b>
                <i>%s</i>
                """;

        final AtomicInteger counter = new AtomicInteger(1);
        final String title = readingExercise.title();
        final String text = String.join("\n\n", readingExercise.paragraphs().paragraphs());
        final String answers = readingExercise.tasks()
                .tasks()
                .stream()
                .map(task -> String.format(answersTemplate, counter.getAndIncrement() + ". " + task.question(), task.answer()))
                .collect(Collectors.joining("\n"));

        final String readingExerciseOutput = String.format(readingExerciseTemplate, title, text, answers);

        SendMessage sendMessage = SendMessage
                .builder()
                .chatId(chatId)
                .text(readingExerciseOutput)
                .parseMode("HTML")
                .build();

        var document = readingExercise.document().onePage() ? toInputMediaPhoto(chatId, readingExercise.document().getBinaryImages().getFirst()) : null;
        var documents = Objects.isNull(document) ? toInputMediaPhotos(chatId, readingExercise.document().getBinaryImages()) : null;

        try {
            telegramClient.execute(sendMessage);
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
    }

    private SendMediaGroup toInputMediaPhotos(Long chatId, List<byte[]> binaryImages) {
        var builder = SendMediaGroup.builder().chatId(chatId);
        int index = 0;
        for (byte[] imageData : binaryImages) {
            InputMedia inputMedia = new InputMediaPhoto(new ByteArrayInputStream(imageData), "" + index);
            builder.media(inputMedia);

            index++;
        }
        return builder.build();
    }

    private SendPhoto toInputMediaPhoto(Long chatId, byte[] binaryImage) {
        return SendPhoto.builder()
                .chatId(chatId)
                .photo(new InputFile(new ByteArrayInputStream(binaryImage), "___.png"))
                .build();
    }

    private record ImageCaption(String caption, String parseMode) {
    }
}
