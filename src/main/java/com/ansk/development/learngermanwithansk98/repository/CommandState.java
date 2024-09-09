package com.ansk.development.learngermanwithansk98.repository;

import com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel;
import org.apache.commons.lang3.StringUtils;

/**
 * Represents the state of a command that is stored in {@link CommandCache}.
 * This class encapsulates the {@link AbstractCommandModel} associated with the command
 * and tracks the awaiting key, which represent a state of a current command.
 *
 * @author Anton Skripin
 */
public class CommandState {
    private final AbstractCommandModel<?> currentCommandModel;
    private String awaitingKey;

    /**
     * Constructor.
     *
     * @param currentCommandModel current model required to process a command
     */
    public CommandState(AbstractCommandModel<?> currentCommandModel) {
        this.currentCommandModel = currentCommandModel;
    }

    /**
     * Sets the awaiting key, which could represents the state of a command.
     *
     * @param awaitingKey the key of the command that is to be received next
     */
    public void setAwaitingKey(String awaitingKey) {
        this.awaitingKey = awaitingKey;
    }

    /**
     * Returns the current model of the current command.
     *
     * @return current command model
     */
    public AbstractCommandModel<?> getCurrentCommandModel() {
        return currentCommandModel;
    }

    /**
     * Gets awaiting key of the command.
     *
     * @return awaiting key
     */
    public String getAwaitingKey() {
        return awaitingKey;
    }

    /**
     * Checks whether there is an awaiting key for this command.
     *
     * @return {@code true} if there is a non-empty awaiting key, {@code false} otherwise
     */
    public boolean hasAwaitingKey() {
        return StringUtils.isNotEmpty(awaitingKey);
    }
}
