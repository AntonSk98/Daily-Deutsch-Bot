package com.ansk.development.learngermanwithansk98.domain;

import com.ansk.development.learngermanwithansk98.service.Action;

public class Session {
    private Action currentAction;
    private String statePayload;

    private Session(Action currentAction) {
        this.currentAction = currentAction;
    }

    public static Session of(Action currentAction) {
        return new Session(currentAction);
    }

    public Action getCurrentAction() {
        return currentAction;
    }

    public void setCurrentAction(Action currentAction) {
        this.currentAction = currentAction;
    }
}
