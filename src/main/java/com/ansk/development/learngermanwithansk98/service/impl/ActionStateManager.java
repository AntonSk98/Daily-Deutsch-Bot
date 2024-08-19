package com.ansk.development.learngermanwithansk98.service.impl;

import com.ansk.development.learngermanwithansk98.config.ActionDefinition;
import com.ansk.development.learngermanwithansk98.config.ActionsConfiguration;
import com.ansk.development.learngermanwithansk98.domain.Session;
import com.ansk.development.learngermanwithansk98.repository.SessionRepository;
import com.ansk.development.learngermanwithansk98.service.Action;
import com.ansk.development.learngermanwithansk98.service.api.IActionStateManager;
import org.springframework.stereotype.Service;

/**
 * Implementation of {@link IActionStateManager}.
 *
 * @author Anton Skripin
 */
@Service
public class ActionStateManager implements IActionStateManager {

    private final SessionRepository sessionRepository;
    private final ActionsConfiguration actionsConfiguration;

    public ActionStateManager(SessionRepository sessionRepository,
                              ActionsConfiguration actionsConfiguration) {
        this.sessionRepository = sessionRepository;
        this.actionsConfiguration = actionsConfiguration;
    }

    @Override
    public ActionDefinition getCurrentActionDefinition() {
        return actionsConfiguration.findActionState(getCurrentAction());
    }

    @Override
    public Action getCurrentAction() {
        Session currentSession = sessionRepository.getSessionOrDefault(actionsConfiguration::findInitAction);
        return currentSession.getCurrentAction();
    }

    @Override
    public Action moveToNextAction() {
        Session currentSession = sessionRepository.getSessionOrDefault(actionsConfiguration::findInitAction);
        Action nextAction = actionsConfiguration.findNextAction(currentSession.getCurrentAction());
        sessionRepository.setSession(Session.of(nextAction));
        return sessionRepository.getSession().getCurrentAction();
    }

    @Override
    public Action moveToPreviousAction() {
        Session currentSession = sessionRepository.getSessionOrDefault(actionsConfiguration::findInitAction);
        Action previousAction = actionsConfiguration.findPreviousAction(currentSession.getCurrentAction());
        sessionRepository.setSession(Session.of(previousAction));
        return sessionRepository.getSession().getCurrentAction();
    }
}
