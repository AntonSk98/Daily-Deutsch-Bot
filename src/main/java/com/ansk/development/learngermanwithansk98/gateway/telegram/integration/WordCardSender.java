package com.ansk.development.learngermanwithansk98.gateway.telegram.integration;

import com.ansk.development.learngermanwithansk98.service.model.output.ExerciseDocument;
import org.springframework.util.CollectionUtils;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.Optional;

import static com.ansk.development.learngermanwithansk98.gateway.telegram.integration.TelegramSenderSupport.documentSender;

/**
 * Component to send a card with words.
 *
 * @author Anton Skripin
 */
public class WordCardSender {

    private static final String WORD_CARD_TEMPLATE = """
            ⭐️ #Words
            
            📝 Expand your vocabulary with these new words! ⬆️
            """;

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
     * Sends a word card.
     *
     * @param chatId           char id
     * @param exerciseDocument document with words
     */
    public void sendWordCard(Long chatId, ExerciseDocument exerciseDocument) {
        var documentCaption = new TelegramSenderSupport.DocumentCaption(WORD_CARD_TEMPLATE, "HTML");
        var mediaParameters = new TelegramSenderSupport.DocumentRenderingParams(Optional.of(documentCaption), false);

        if (CollectionUtils.isEmpty(exerciseDocument.pages())) {
            throw new IllegalStateException("No exercise document is passed as a parameter!");
        }

        documentSender(chatId, exerciseDocument, mediaParameters).accept(telegramClient);
    }


}
