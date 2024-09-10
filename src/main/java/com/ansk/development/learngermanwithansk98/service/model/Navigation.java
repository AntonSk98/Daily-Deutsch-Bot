package com.ansk.development.learngermanwithansk98.service.model;

/**
 * Represents a navigation direction with associated text and command.
 *
 * @author Anton Skripin
 */
public enum Navigation {
    PREVIOUS("<-", "/previous"),
    NEXT("->", "/next");

    private final String direction;
    private final String command;

    /**
     * Constructor.
     *
     * @param direction navigation direction
     * @param command   command
     */
    Navigation(String direction, String command) {
        this.direction = direction;
        this.command = command;
    }

    public String getText() {
        return direction;
    }

    public String getCommand() {
        return command;
    }

    /**
     * Returns whether the command shall be navigated forward.
     *
     * @return true if navigation forward
     */
    public boolean isNext() {
        return this == NEXT;
    }

    /**
     * Returns whether the command shall be navigated backward.
     *
     * @return true if navigation backward
     */
    public boolean isPrevious() {
        return this == PREVIOUS;
    }

    /**
     * Get next navigation
     *
     * @return next navigation
     */
    public static Navigation next() {
        return NEXT;
    }

    /**
     * Get previous navigation
     *
     * @return previous navigation
     */
    public static Navigation previous() {
        return PREVIOUS;
    }
}
