package com.ansk.development.learngermanwithansk98.config;

import com.ansk.development.learngermanwithansk98.service.Action;

/**
 * The {@code ActionDefinition} class represents the state of an action within the process of creating a card.
 * Each action state defines a specific step that must be performed, whether the step is required, and
 * the next action to transition to, if any.
 *
 * @author Anton Skripin
 */
public class ActionDefinition {
    private Action action;
    private Action goTo;
    private boolean required;

    public Action getAction() {
        return action;
    }

    public Action getGoTo() {
        return goTo;
    }

    public boolean isRequired() {
        return required;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public void setGoTo(Action goTo) {
        this.goTo = goTo;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}
