package com.ansk.development.learngermanwithansk98.service.model;

public class CommandParameters {

    private String input;
    private Long chatId;

    private CommandParameters() {

    }

    public static CommandParameters create() {
        return new CommandParameters();
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

}
