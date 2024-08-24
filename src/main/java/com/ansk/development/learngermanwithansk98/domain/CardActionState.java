package com.ansk.development.learngermanwithansk98.domain;

import com.ansk.development.learngermanwithansk98.service.CardDefinitionAction;

public class CardActionState {
    private CardDefinitionAction currentCardDefinitionAction;
    private String statePayload;

    private CardActionState(CardDefinitionAction currentCardDefinitionAction) {
        this.currentCardDefinitionAction = currentCardDefinitionAction;
    }

    public static CardActionState of(CardDefinitionAction currentCardDefinitionAction) {
        return new CardActionState(currentCardDefinitionAction);
    }

    public CardDefinitionAction getCurrentAction() {
        return currentCardDefinitionAction;
    }

    public void setCurrentAction(CardDefinitionAction currentCardDefinitionAction) {
        this.currentCardDefinitionAction = currentCardDefinitionAction;
    }
}
