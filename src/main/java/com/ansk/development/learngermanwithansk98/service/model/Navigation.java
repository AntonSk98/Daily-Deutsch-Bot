package com.ansk.development.learngermanwithansk98.service.model;

/**
 * Represents a navigation direction with associated text and command.
 *
 * @author Anton Skripin
 */
public enum Navigation {
    PREVIOUS("<-", "/previous"),
    NEXT("->", "/next");

    private final String text;
    private final String command;

    Navigation(String text, String command) {
        this.text = text;
        this.command = command;
    }

    public String getText() {
        return text;
    }

    public String getCommand() {
        return command;
    }

    public boolean isNext() {
        return this == NEXT;
    }

    public boolean isPrevious() {
        return this == PREVIOUS;
    }

    public static Navigation next() {
        return NEXT;
    }

    public static Navigation previous() {
        return PREVIOUS;
    }
}
