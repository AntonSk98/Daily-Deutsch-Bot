package com.ansk.development.learngermanwithansk98.service.model;

import com.ansk.development.learngermanwithansk98.service.CardDefinitionAction;

/**
 * Represents the payload for an {@link CardDefinitionAction}
 *
 * @author Anton Skripin
 */
public class Payload {
    private final String value;

    private Payload(String value) {
        this.value = value;
    }

    public static Payload init(String value) {
        return new Payload(value);
    }

    public String getValue() {
        return value;
    }
}
