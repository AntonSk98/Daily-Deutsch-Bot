package com.ansk.development.learngermanwithansk98.service.api;

import com.ansk.development.learngermanwithansk98.service.CardDefinitionAction;

/**
 * Entry point that finds {@link ICardActionHandler} by the action stored in the session.
 *
 * @author Anton Skripin
 */
public interface IActionHandlerService {
    ICardActionHandler findHandlerBy(CardDefinitionAction cardDefinitionAction);
}
