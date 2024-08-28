package com.ansk.development.learngermanwithansk98.gateway;

import com.ansk.development.learngermanwithansk98.config.DailyDeutschBotConfiguration;
import com.ansk.development.learngermanwithansk98.service.model.Images;
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

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

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

        this.telegramClient = new OkHttpTelegramClient(client, config.getToken());
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

    public void sendImages(Long chatId, Images images) {
        if (CollectionUtils.isEmpty(images.getBinaryImages())) {
            throw new IllegalStateException("No images passed as a parameter!");
        }

        try {
            if (images.getBinaryImages().size() == 1) {
                sendImage(chatId, images.getBinaryImages().stream().findFirst().get());
                return;
            }

            sendImageCollections(chatId, images.getBinaryImages());
        } catch (TelegramApiException e) {
            throw new IllegalStateException("Failed to send images to the client", e);
        }
    }

    private void sendImageCollections(Long chatId, List<byte[]> binaryImages) throws TelegramApiException {
        var builder = SendMediaGroup.builder().chatId(chatId);
        int index = 0;
        for (byte[] imageData : binaryImages) {
            InputMedia inputMedia = new InputMediaPhoto(new ByteArrayInputStream(imageData), "" + index);
            builder.media(inputMedia);
            index++;
        }
        telegramClient.execute(builder.build());
    }

    private void sendImage(Long chatId, byte[] binaryImage) throws TelegramApiException {
        SendPhoto sendPhoto = SendPhoto.builder()
                .chatId(chatId)
                .photo(new InputFile(new ByteArrayInputStream(binaryImage), "___.png"))
                .build();

//        SendDocument sendDocument = SendDocument.builder()
//                .chatId(chatId)
//                .document(new InputFile(new ByteArrayInputStream(binaryImage), "___.png"))
//                .build();

        telegramClient.execute(sendPhoto);
    }

    private void test(byte[] binaryImage) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(binaryImage);
        try {
            BufferedImage bufferedImage = ImageIO.read(byteArrayInputStream);
            File outputFile = new File("/media/ansk98/D/development/learn-german-with-ansk98/src/test/a.png");
            ImageIO.write(bufferedImage, "PNG", outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
