package com.ansk.development.learngermanwithansk98.service.model.input;

import com.ansk.development.learngermanwithansk98.service.model.Navigation;

/**
 * A builder-style class used to encapsulate command parameters for further command processing.
 *
 * @author Anton Skripin
 */
public class CommandParameters {

    private String input;
    private Navigation navigation;
    private Long chatId;

    private CommandParameters() {

    }

    /**
     * Creates and returns a new instance of {@code CommandParameters}.
     *
     * @return a new {@code CommandParameters} instance
     */
    public static CommandParameters create() {
        return new CommandParameters();
    }

    /**
     * Adds navigation details to the command parameters.
     *
     * @param navigation the {@link Navigation} object to associate with this command
     * @return the current instance of {@code CommandParameters} with updated navigation
     */
    public CommandParameters addNavigation(Navigation navigation) {
        this.navigation = navigation;
        return this;
    }

    /**
     * Sets the input value for the command parameters.
     *
     * @param input the input string to associate with the command
     * @return the current instance of {@code CommandParameters} with updated input
     */
    public CommandParameters withInput(String input) {
        this.input = input;
        return this;
    }

    /**
     * Sets the chat ID for the command parameters.
     *
     * @param chatId the chat ID to associate with the command
     * @return the current instance of {@code CommandParameters} with updated chat ID
     */
    public CommandParameters withChatId(Long chatId) {
        this.chatId = chatId;
        return this;
    }

    /**
     * Retrieves the input value associated with the command.
     *
     * @return the input value as a {@link String}
     */
    public String input() {
        return input;
    }

    /**
     * Retrieves the chat ID associated with the command.
     *
     * @return the chat ID as a {@link Long}
     */
    public Long chatId() {
        return chatId;
    }

    /**
     * Retrieves the navigation details associated with the command.
     *
     * @return the {@link Navigation} object associated with the command
     */
    public Navigation navigation() {
        return navigation;
    }

}
