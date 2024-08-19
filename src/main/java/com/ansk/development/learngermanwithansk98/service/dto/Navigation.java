package com.ansk.development.learngermanwithansk98.service.dto;

import com.ansk.development.learngermanwithansk98.service.Action;

/**
 * Represents navigation options for an {@link Action}.
 */
public class Navigation {
    private Boolean toNextAction;
    private Boolean toPreviousAction;

    private Navigation() {

    }

    public static Navigation init() {
        return new Navigation();
    }

    public Navigation toNextAction() {
        this.toNextAction = true;
        this.toPreviousAction = false;
        return this;
    }

    public Navigation toPreviousAction() {
        this.toPreviousAction = true;
        this.toNextAction = false;
        return this;
    }

    public Boolean shouldNavigateToNextAction() {
        return toNextAction;
    }

    public Boolean shouldNavigateToPreviousAction() {
        return toPreviousAction;
    }
}
