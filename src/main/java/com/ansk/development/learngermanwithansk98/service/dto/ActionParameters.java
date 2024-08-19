package com.ansk.development.learngermanwithansk98.service.dto;

/**
 * Represents parameters for an action, encapsulating navigation and payload information.
 *
 * @author Anton Skripin
 */
public class ActionParameters {
    private Navigation navigation;
    private Payload payload;

    private ActionParameters() {

    }

    private static ActionParameters init() {
        return new ActionParameters();
    }

    public ActionParameters withNavigation(Navigation navigation) {
        this.navigation = navigation;
        return this;
    }

    public ActionParameters withPayload(Payload payload) {
        this.payload = payload;
        return this;
    }

    public Navigation getNavigation() {
        return navigation;
    }

    public Payload getPayload() {
        return payload;
    }
}
