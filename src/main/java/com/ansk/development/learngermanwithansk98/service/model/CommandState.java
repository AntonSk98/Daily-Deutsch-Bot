package com.ansk.development.learngermanwithansk98.service.model;

import com.ansk.development.learngermanwithansk98.repository.CommandCache;

/**
 * State of a command that is stored in {@link CommandCache}.
 *
 * @author Anton Skripin
 */
public class CommandState {
    private final AbstractCommandModel<?> currentCommandModel;
    private String awaitingKey;

    public CommandState(AbstractCommandModel<?> currentCommandModel) {
        this.currentCommandModel = currentCommandModel;
    }

    public void setAwaitingKey(String awaitingKey) {
        this.awaitingKey = awaitingKey;
    }

    public AbstractCommandModel<?> getCurrentCommandModel() {
        return currentCommandModel;
    }

    public String getAwaitingKey() {
        return awaitingKey;
    }
}
