package com.ansk.development.learngermanwithansk98.service.model;

/**
 * Defines all known commands.
 *
 * @author Anton Skripin
 */
public enum Command {
    PROFILE("/profile");

    private final String path;

    Command(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
