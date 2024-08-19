package com.ansk.development.learngermanwithansk98.service.api;

import com.ansk.development.learngermanwithansk98.config.ActionDefinition;
import com.ansk.development.learngermanwithansk98.service.Action;

/**
 * Service that provides methods to navigate and execute {@link Action}s.
 *
 * @author Anton Skripin
 */
public interface IActionStateManager {
    ActionDefinition getCurrentActionDefinition();
    Action getCurrentAction();
    Action moveToNextAction();
    Action moveToPreviousAction();
}
