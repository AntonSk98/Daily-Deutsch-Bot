package com.ansk.development.learngermanwithansk98.exception;

import com.ansk.development.learngermanwithansk98.integration.telegram.ITelegramClient;
import org.springframework.stereotype.Component;

/**
 * Global error handler that notifies the admin about an exception that happened.
 *
 * @author Anton Skripin
 */
@Component
public class CommandExceptionHandler {
    
    private final ITelegramClient outputGateway;

    /**
     * Constructor.
     * @param outputGateway See {@link ITelegramClient}
     */
    public CommandExceptionHandler(ITelegramClient outputGateway) {
        this.outputGateway = outputGateway;
    }

    /**
     * Notifies the admin about the error.
     * @param chatId chat id
     * @param ex exception
     */
    public void handleGlobalException(Long chatId, Exception ex) {
        outputGateway.sendErrorMessage(chatId, ex.getClass(), ex.getMessage());
        throw new RuntimeException(ex);
    }
}
