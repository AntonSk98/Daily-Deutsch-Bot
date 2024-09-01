package com.ansk.development.learngermanwithansk98.service.model.input;

import com.ansk.development.learngermanwithansk98.service.model.Navigation;

public class CommandParameters {

    private String input;
    private Navigation navigation;
    private Long chatId;

    private CommandParameters() {

    }

    public static CommandParameters create() {
        return new CommandParameters();
    }

    public CommandParameters addNavigation(Navigation navigation) {
        this.navigation = navigation;
        return this;
    }

    public CommandParameters withInput(String input) {
        this.input = input;
        return this;
    }

    public CommandParameters withChatId(Long chatId) {
        this.chatId = chatId;
        return this;
    }

    public String input() {
        return input;
    }

    public Long chatId() {
        return chatId;
    }

    public Navigation navigation() {
        return navigation;
    }

}
