package com.ansk.development.learngermanwithansk98.service.impl;

import com.ansk.development.learngermanwithansk98.config.CardActionDefinition;
import com.ansk.development.learngermanwithansk98.config.CardActionsConfiguration;
import com.ansk.development.learngermanwithansk98.domain.CardActionState;
import com.ansk.development.learngermanwithansk98.repository.CardActionCache;
import com.ansk.development.learngermanwithansk98.service.CardDefinitionAction;
import com.ansk.development.learngermanwithansk98.service.api.IActionStateManager;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link IActionStateManager}.
 *
 * @author Anton Skripin
 */
@Service
public class ActionStateManager implements IActionStateManager {

    private final CardActionCache cardActionCache;
    private final CardActionsConfiguration cardActionsConfiguration;

    public ActionStateManager(CardActionCache cardActionCache,
                              CardActionsConfiguration cardActionsConfiguration) {
        this.cardActionCache = cardActionCache;
        this.cardActionsConfiguration = cardActionsConfiguration;
    }

    @Override
    public CardActionDefinition getCurrentActionDefinition() {
        return cardActionsConfiguration.findActionState(getCurrentAction());
    }

    @Override
    public CardDefinitionAction getCurrentAction() {
        CardActionState currentCardActionState = cardActionCache.getStateOrDefault(cardActionsConfiguration::findInitAction);
        return currentCardActionState.getCurrentAction();
    }

    @Override
    public CardDefinitionAction moveToNextAction() {
        CardActionState currentCardActionState = cardActionCache.getStateOrDefault(cardActionsConfiguration::findInitAction);
        CardDefinitionAction nextCardDefinitionAction = cardActionsConfiguration.findNextAction(currentCardActionState.getCurrentAction());
        cardActionCache.setState(CardActionState.of(nextCardDefinitionAction));
        return cardActionCache.getState().getCurrentAction();
    }

    @Override
    public CardDefinitionAction moveToPreviousAction() {
        CardActionState currentCardActionState = cardActionCache.getStateOrDefault(cardActionsConfiguration::findInitAction);
        CardDefinitionAction previousCardDefinitionAction = cardActionsConfiguration.findPreviousAction(currentCardActionState.getCurrentAction());
        cardActionCache.setState(CardActionState.of(previousCardDefinitionAction));
        return cardActionCache.getState().getCurrentAction();
    }
}
