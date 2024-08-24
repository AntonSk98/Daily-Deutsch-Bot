package com.ansk.development.learngermanwithansk98.service.api;

import com.ansk.development.learngermanwithansk98.service.CardDefinitionAction;
import com.ansk.development.learngermanwithansk98.service.model.ActionParameters;

/**
 * Defines a handler for a particular {@link CardDefinitionAction}.
 *
 * @author Anton Skripin
 */
public interface ICardActionHandler {

    CardDefinitionAction getSupportedAction();

    void executeAction(ActionParameters parameters);
}
