package com.ansk.development.learngermanwithansk98.config;

import com.ansk.development.learngermanwithansk98.service.Action;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.*;

/**
 * Configuration to define {@link ActionDefinition}s.
 * For more detail, see {@link ActionDefinition}.
 *
 * @author Anton Skripin
 */
@Configuration
@ConfigurationProperties(prefix = "state")
public class ActionsConfiguration {
    List<ActionDefinition> actions = new ArrayList<>();

    public List<ActionDefinition> getActions() {
        return Collections.unmodifiableList(actions);
    }

    public void setActions(LinkedList<ActionDefinition> actions) {
        this.actions = actions;
    }


    public Action findInitAction() {
        return actions.getFirst().getAction();
    }

    public ActionDefinition findActionState(Action action) {
        return actions.stream()
                .filter(actionDefinition -> actionDefinition.getAction().equals(action))
                .findFirst()
                .orElseThrow();
    }

    public Action findNextAction(Action currentAction) {
        ActionDefinition currentActionDefinition = findActionState(currentAction);

        if (Objects.nonNull(currentActionDefinition.getGoTo())) {
            return currentActionDefinition.getGoTo();
        }

        int currentActionIndex = actions.indexOf(currentActionDefinition);

        if (currentActionIndex < actions.size() - 1) {
            return actions.get(currentActionIndex + 1).getAction();
        }

        return currentAction;
    }

    public Action findPreviousAction(Action currentAction) {
        ActionDefinition currentActionDefinition = findActionState(currentAction);

        int currentActionIndex = actions.indexOf(currentActionDefinition);

        if (currentActionIndex > 0) {
            return actions.get(currentActionIndex - 1).getAction();
        }

        return currentAction;
    }
}
