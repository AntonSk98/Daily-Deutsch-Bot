package com.ansk.development.learngermanwithansk98.service.api;

import com.ansk.development.learngermanwithansk98.service.Action;

/**
 * Entry point that finds {@link IActionHandler} by the action stored in the session.
 *
 * @author Anton Skripin
 */
public interface IActionHandlerService {
    IActionHandler findHandlerBy(Action action);
}
