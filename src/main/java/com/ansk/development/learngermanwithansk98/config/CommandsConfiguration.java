package com.ansk.development.learngermanwithansk98.config;

import com.ansk.development.learngermanwithansk98.service.model.Command;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Encapsulates generic configuration required to handle a {@link Command}.
 *
 * @author Anton Skripin
 */
@ConfigurationProperties(prefix = "commands")
public record CommandsConfiguration(List<CommandDefinition> commandDefinition) {

    public CommandDefinition.Parameter findParameter(String path, String key) {
        return findCommand(path)
                .parameters()
                .stream()
                .filter(parameter -> parameter.key().equals(key))
                .findFirst()
                .orElseThrow();
    }

    public CommandDefinition findCommand(String path) {
        return this.commandDefinition.stream()
                .filter(commandDefinition -> commandDefinition.path().equals(path))
                .findFirst()
                .orElseThrow();
    }

    public static record CommandDefinition(
            String path,
            boolean withNavigation,
            List<Parameter> parameters) {

        public static record Parameter(
                String key,
                String prompt,
                boolean required) {
        }
    }
}
