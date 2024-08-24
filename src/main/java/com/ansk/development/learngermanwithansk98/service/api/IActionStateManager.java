package com.ansk.development.learngermanwithansk98.service.api;

import com.ansk.development.learngermanwithansk98.config.CardActionDefinition;
import com.ansk.development.learngermanwithansk98.service.CardDefinitionAction;

/**
 * Service that provides methods to navigate and execute {@link CardDefinitionAction}s.
 *
 * @author Anton Skripin
 */
public interface IActionStateManager {
    CardActionDefinition getCurrentActionDefinition();
    CardDefinitionAction getCurrentAction();
    CardDefinitionAction moveToNextAction();
    CardDefinitionAction moveToPreviousAction();
}
