package com.ansk.development.learngermanwithansk98.gateway;

import com.ansk.development.learngermanwithansk98.config.DailyDeutschBotConfiguration;
import com.ansk.development.learngermanwithansk98.service.model.output.ExerciseDocument;
import com.ansk.development.learngermanwithansk98.service.model.output.ListeningExercise;
import com.ansk.development.learngermanwithansk98.service.model.output.ReadingExercise;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import okhttp3.OkHttpClient;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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
    private final DailyDeutschBotConfiguration configuration;

    public OutputGateway(DailyDeutschBotConfiguration config,
                         ObjectMapper objectMapper) {

        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

        this.telegramClient = new OkHttpTelegramClient(client, config.token());
        this.objectMapper = objectMapper;
        this.configuration = config;
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


    public void sendWordCard(Long chatId, ExerciseDocument exerciseDocument) {
        if (CollectionUtils.isEmpty(exerciseDocument.pages())) {
            throw new IllegalStateException("No images passed as a parameter!");
        }
        Runnable sendMediaGroup = sendMediaGroup(chatId, exerciseDocument, MediaParameters.NO_MEDIA_PARAMS);
        sendMediaGroup.run();
    }

    public void sendReadingExercise(Long chatId, ReadingExercise readingExercise) {
        MediaParameters mediaParameters = readingMediaParameters();
        List<SendMessage> readingExerciseMessages = toReadingExercise(chatId, readingExercise);
        Runnable sendMediaGroup = sendMediaGroup(chatId, readingExercise.document(), mediaParameters);
        try {
            for (SendMessage readingExerciseMessage : readingExerciseMessages) {
                telegramClient.execute(readingExerciseMessage);
            }
            sendMediaGroup.run();
        } catch (TelegramApiException e) {
            throw new IllegalStateException("Error occurred while sending reading exercise", e);
        }
    }

    public void sendWritingExercise(Long chatId, String topic, ExerciseDocument writingExerciseDocument) {
        final String writingExerciseTemplate = """
                ⭐️ #Writing
                
                🗒️ Please write your opinion on the following topic:
                
                <b>%s</b>
                
                Also feel free to check out our sample text ⬆️
                """;

        final String writingExercise = String.format(writingExerciseTemplate, topic);

        MediaParameters mediaParameters = new MediaParameters(
                Optional.of(new ImageCaption(writingExercise, "HTML")),
                true
        );

        Runnable sendMediaGroup = sendMediaGroup(chatId, writingExerciseDocument, mediaParameters);
        sendMediaGroup.run();
    }

    public InputStream audioStream(String audio) {
        final String path = "https://api.telegram.org/file/%s/%s";
        GetFile audioMetadata = new GetFile(audio);
        try {
            File audioFile = telegramClient.execute(audioMetadata);
            return new URL(audioFile.getFileUrl(configuration.token())).openStream();
        } catch (TelegramApiException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendListeningExercise(Long chatId, ListeningExercise listeningExercise) {
        InputStream audioStream = audioStream(listeningExercise.audio());
        InputFile audioFile = new InputFile(audioStream, "audio.mp3");
        SendAudio sendAudio = SendAudio.builder()
                .chatId(chatId)
                .audio(audioFile)
                .caption("Here is an exercise")
                .build();


        Runnable mediaGroup = sendMediaGroup(chatId, listeningExercise.document(), MediaParameters.NO_MEDIA_PARAMS);

        try {
            telegramClient.execute(sendAudio);
            mediaGroup.run();
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private Runnable sendMediaGroup(Long chatId, ExerciseDocument exerciseDocument, MediaParameters mediaParameters) {

        return () -> {
            var document = exerciseDocument.onePage()
                    ? toInputMediaPhoto(chatId, exerciseDocument.pages().getFirst(), mediaParameters)
                    : null;
            var documents = Objects.isNull(document)
                    ? toInputMediaPhotos(chatId, exerciseDocument.pages(), mediaParameters)
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

    private MediaParameters readingMediaParameters() {
        final String documentCaption = """
                🖨️ #ReadingForPrinting
                📚 Here’s the reading exercise formatted for printing.
                """;

        var caption = new ImageCaption(documentCaption, "HTML");

        return new MediaParameters(Optional.of(caption), false);
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

    private SendMediaGroup toInputMediaPhotos(Long chatId, List<byte[]> binaryImages, MediaParameters mediaParameters) {
        var builder = SendMediaGroup.builder().chatId(chatId);
        int index = 0;
        for (byte[] imageData : binaryImages) {
            InputMediaPhoto inputMedia = new InputMediaPhoto(new ByteArrayInputStream(imageData), "" + index);
            inputMedia.setHasSpoiler(mediaParameters.withSpoiler());
            builder.media(inputMedia);
            if (index == 0 && mediaParameters.imageCaption().isPresent()) {
                inputMedia.setCaption(mediaParameters.imageCaption().get().caption);
                inputMedia.setParseMode(mediaParameters.imageCaption().get().parseMode);
            }
            index++;
        }
        return builder.build();
    }

    private SendPhoto toInputMediaPhoto(Long chatId, byte[] binaryImage, MediaParameters mediaParameters) {
        return SendPhoto.builder()
                .chatId(chatId)
                .photo(new InputFile(new ByteArrayInputStream(binaryImage), "___.png"))
                .caption(mediaParameters.imageCaption().map(ImageCaption::caption).orElse(null))
                .hasSpoiler(mediaParameters.withSpoiler())
                .parseMode(mediaParameters.imageCaption().map(ImageCaption::parseMode).orElse(null))
                .build();
    }

    private record ImageCaption(String caption, String parseMode) {
    }

    private record MediaParameters(Optional<ImageCaption> imageCaption, boolean withSpoiler) {
        private static final MediaParameters NO_MEDIA_PARAMS = new MediaParameters(Optional.empty(), false);
    }

}
