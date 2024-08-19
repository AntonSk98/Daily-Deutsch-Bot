package com.ansk.development.learngermanwithansk98.service.api;

import com.ansk.development.learngermanwithansk98.service.Action;

/**
 * Defines a handler for a particular {@link Action}.
 *
 * @author Anton Skripin
 */
public interface IActionHandler {

    Action getSupportedAction();

    void executeAction();
}
