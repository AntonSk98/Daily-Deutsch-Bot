package com.ansk.development.learngermanwithansk98.config;

import com.ansk.development.learngermanwithansk98.service.CardDefinitionAction;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.*;

/**
 * Configuration to define {@link CardActionDefinition}s.
 * For more detail, see {@link CardActionDefinition}.
 *
 * @author Anton Skripin
 */
@Configuration
@ConfigurationProperties(prefix = "card-definition")
public class CardActionsConfiguration {
    List<CardActionDefinition> actions = new ArrayList<>();

    public List<CardActionDefinition> getActions() {
        return Collections.unmodifiableList(actions);
    }

    public void setActions(LinkedList<CardActionDefinition> actions) {
        this.actions = actions;
    }


    public CardDefinitionAction findInitAction() {
        return actions.getFirst().getAction();
    }

    public CardActionDefinition findActionState(CardDefinitionAction cardDefinitionAction) {
        return actions.stream()
                .filter(cardActionDefinition -> cardActionDefinition.getAction().equals(cardDefinitionAction))
                .findFirst()
                .orElseThrow();
    }

    public CardDefinitionAction findNextAction(CardDefinitionAction currentCardDefinitionAction) {
        CardActionDefinition currentCardActionDefinition = findActionState(currentCardDefinitionAction);

        if (Objects.nonNull(currentCardActionDefinition.getGoTo())) {
            return currentCardActionDefinition.getGoTo();
        }

        int currentActionIndex = actions.indexOf(currentCardActionDefinition);

        if (currentActionIndex < actions.size() - 1) {
            return actions.get(currentActionIndex + 1).getAction();
        }

        return currentCardDefinitionAction;
    }

    public CardDefinitionAction findPreviousAction(CardDefinitionAction currentCardDefinitionAction) {
        CardActionDefinition currentCardActionDefinition = findActionState(currentCardDefinitionAction);

        int currentActionIndex = actions.indexOf(currentCardActionDefinition);

        if (currentActionIndex > 0) {
            return actions.get(currentActionIndex - 1).getAction();
        }

        return currentCardDefinitionAction;
    }
}
