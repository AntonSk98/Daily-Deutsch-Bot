package com.ansk.development.learngermanwithansk98.gateway.telegram.integration;

import com.ansk.development.learngermanwithansk98.service.model.output.ExerciseDocument;
import org.springframework.util.CollectionUtils;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import static com.ansk.development.learngermanwithansk98.gateway.telegram.integration.TelegramSenderSupport.documentSender;

/**
 * Component to send a card with words.
 *
 * @author Anton Skripin
 */
public class WordCardSender {

    private final TelegramClient telegramClient;

    /**
     * Constructor.
     *
     * @param telegramClient See {@link TelegramClient}
     */
    public WordCardSender(TelegramClient telegramClient) {
        this.telegramClient = telegramClient;
    }

    /**
     * Sends a word card
     *
     * @param chatId           char id
     * @param exerciseDocument document with words
     */
    public void sendWordCard(Long chatId, ExerciseDocument exerciseDocument) {
        if (CollectionUtils.isEmpty(exerciseDocument.pages())) {
            throw new IllegalStateException("No exercise document is passed as a parameter!");
        }

        documentSender(chatId, exerciseDocument, TelegramSenderSupport.DocumentRenderingParams.NO_RENDERING_PARAMS).accept(telegramClient);
    }


}
