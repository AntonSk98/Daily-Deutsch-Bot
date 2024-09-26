package com.ansk.development.learngermanwithansk98.service.model.input;

/**
 * Common model to check whether an action is confirmed and should be done.
 *
 * @author Anton Skripin
 */
public interface IConfirmationModel {

    /**
     * Attempts to parse the input to the boolean value whether an action should be done.
     *
     * @param value value
     */
    void parseValue(String value);

    /**
     * True if an action should be performed
     *
     * @return true or false depending on whether an action should be performed
     */
    boolean shouldDo();
}
