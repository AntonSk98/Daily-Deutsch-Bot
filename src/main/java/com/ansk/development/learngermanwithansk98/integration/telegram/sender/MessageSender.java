package com.ansk.development.learngermanwithansk98.integration.telegram.sender;

import com.ansk.development.learngermanwithansk98.service.model.output.InformationPostModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import static com.ansk.development.learngermanwithansk98.service.model.Navigation.NEXT;
import static com.ansk.development.learngermanwithansk98.service.model.Navigation.PREVIOUS;

/**
 * A component responsible for sending plain messages via Telegram.
 *
 * @author Anton Skripin
 */
public class MessageSender {

    private final TelegramClient telegramClient;
    private final ObjectMapper objectMapper;

    /**
     * Constructor.
     *
     * @param telegramClient See {@link TelegramClient}
     * @param objectMapper   See {@link ObjectMapper}
     */
    public MessageSender(TelegramClient telegramClient, ObjectMapper objectMapper) {
        this.telegramClient = telegramClient;
        this.objectMapper = objectMapper;
    }

    /**
     * Sends a plain message.
     *
     * @param chatId  chat id
     * @param message message
     */
    public void sendPlainMessage(Long chatId, String message) {
        try {
            this.telegramClient.execute(SendMessage.builder().chatId(chatId).text(message).build());
        } catch (TelegramApiException e) {
            throw new IllegalStateException("Unexpected error occurred while sending a message to Telegram", e);
        }
    }

    /**
     * Sends a message with navigation buttons.
     *
     * @param chatId  chat id
     * @param message message
     */
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

    /**
     * Sends a message with some preformatted code payload.
     *
     * @param chatId  chat id
     * @param message message
     * @param payload code payload
     * @param <T>     type of the code payload
     */
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

    /**
     * Sends an error message.
     *
     * @param chatId  chat id
     * @param message message
     */
    public void sendErrorMessage(Long chatId, Class<?> clazz, String message) {
        String errorReport = "⚠\uFE0F  <b>Error occurred</b>  ➡\uFE0F"
                + "\n\n" + "<b>Class: </b>"
                + clazz.getSimpleName()
                + "\n"
                + "<b>"
                + "Message: "
                + "</b>"
                + (StringUtils.isEmpty(message) ? "N/A" : message);

        SendMessage sendMessage = SendMessage.builder().chatId(chatId).text(errorReport).parseMode("HTML").build();

        try {
            telegramClient.execute(sendMessage);

        } catch (TelegramApiException e) {
            throw new IllegalStateException("Unexpected error occurred while sending an error", e);
        }
    }

    /**
     * Sends information post.
     *
     * @param chatId          chat id
     * @param informationPost information post
     */
    public void sendInformationPost(Long chatId, InformationPostModel informationPost) {
        final String informationPostTemplate = """
                🔍 🧐  <b>%s</b>
                
                3️⃣... 2️⃣... 1️⃣...  ✍️
                
                %s
                
                🔹🔹🔹
                """;

        SendMessage sendMessage = SendMessage
                .builder()
                .chatId(chatId)
                .text(String.format(informationPostTemplate, informationPost.topic(), informationPost.content()))
                .parseMode("HTML")
                .build();

        try {
            telegramClient.execute(sendMessage);

        } catch (TelegramApiException e) {
            throw new IllegalStateException("Unexpected error occurred while sending information post", e);
        }
    }
}
