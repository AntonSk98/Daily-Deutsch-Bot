package com.ansk.development.learngermanwithansk98.service.impl;

import com.ansk.development.learngermanwithansk98.service.Action;
import com.ansk.development.learngermanwithansk98.service.api.IActionHandler;
import com.ansk.development.learngermanwithansk98.service.api.IActionHandlerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActionHandlerService implements IActionHandlerService {

    private final List<IActionHandler> actionHandlers;

    public ActionHandlerService(List<IActionHandler> actionHandlers) {
        this.actionHandlers = actionHandlers;
    }

    @Override
    public IActionHandler findHandlerBy(Action action) {
        return actionHandlers
                .stream()
                .filter(handler -> handler.getSupportedAction().equals(action))
                .findFirst()
                .orElseThrow();
    }
}
