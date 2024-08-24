package com.ansk.development.learngermanwithansk98.config;

import com.ansk.development.learngermanwithansk98.service.CardDefinitionAction;

/**
 * The {@code ActionDefinition} class represents the state of an action within the process of creating a card.
 * Each action state defines a specific step that must be performed, whether the step is required, and
 * the next action to transition to, if any.
 *
 * @author Anton Skripin
 */
public class CardActionDefinition {
    private CardDefinitionAction cardDefinitionAction;
    private CardDefinitionAction goTo;
    private boolean required;

    public CardDefinitionAction getAction() {
        return cardDefinitionAction;
    }

    public CardDefinitionAction getGoTo() {
        return goTo;
    }

    public boolean isRequired() {
        return required;
    }

    public void setAction(CardDefinitionAction cardDefinitionAction) {
        this.cardDefinitionAction = cardDefinitionAction;
    }

    public void setGoTo(CardDefinitionAction goTo) {
        this.goTo = goTo;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}
