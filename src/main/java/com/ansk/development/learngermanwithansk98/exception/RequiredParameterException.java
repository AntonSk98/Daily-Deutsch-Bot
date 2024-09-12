package com.ansk.development.learngermanwithansk98.exception;

/**
 * Exception that is thrown when a parameter is required but no value for it is provided.
 *
 * @author Anton Skripin
 */
public class RequiredParameterException extends RuntimeException {
    public RequiredParameterException(String message) {
        super(message);
    }
}
