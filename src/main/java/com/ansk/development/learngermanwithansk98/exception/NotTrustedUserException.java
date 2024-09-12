package com.ansk.development.learngermanwithansk98.exception;

/**
 * Exception that is thrown when an unauthorized user is attempting to access the bot.
 *
 * @author Anton Skripin
 */
public class NotTrustedUserException extends RuntimeException {

    /**
     * Constructor.
     *
     * @param message message
     */
    public NotTrustedUserException(String message) {
        super(message);
    }
}
