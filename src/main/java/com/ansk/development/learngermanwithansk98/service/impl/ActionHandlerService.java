package com.ansk.development.learngermanwithansk98.service.impl;

import com.ansk.development.learngermanwithansk98.service.CardDefinitionAction;
import com.ansk.development.learngermanwithansk98.service.api.ICardActionHandler;
import com.ansk.development.learngermanwithansk98.service.api.IActionHandlerService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of {@link IActionHandlerService}.
 *
 * @author Anton Skripin
 */
@Service
public class ActionHandlerService implements IActionHandlerService {

    private final List<ICardActionHandler> actionHandlers;

    public ActionHandlerService(List<ICardActionHandler> actionHandlers) {
        this.actionHandlers = actionHandlers;
    }

    @Override
    public ICardActionHandler findHandlerBy(CardDefinitionAction cardDefinitionAction) {
        return actionHandlers
                .stream()
                .filter(handler -> handler.getSupportedAction().equals(cardDefinitionAction))
                .findFirst()
                .orElseThrow();
    }
}
