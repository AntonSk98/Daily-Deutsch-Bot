package com.ansk.development.learngermanwithansk98.config;

import com.ansk.development.learngermanwithansk98.service.model.Command;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * Encapsulates generic configuration required to handle a {@link Command}.
 *
 * @author Anton Skripin
 */
@ConfigurationProperties(prefix = "commands")
public record CommandsConfiguration(List<CommandDefinition> commandDefinition) {

    /**
     * Finds a parameter by its key within a command's parameters based on the command path.
     *
     * @param path the path of the command to search for.
     * @param key  the key of the parameter to search for.
     * @return the found {@link CommandDefinition.Parameter}.
     */
    public CommandDefinition.Parameter findParameter(String path, String key) {
        return findCommand(path)
                .parameters()
                .stream()
                .filter(parameter -> parameter.key().equals(key))
                .findFirst()
                .orElseThrow();
    }

    /**
     * Finds a command definition based on the provided path.
     *
     * @param path the path of the command to search for.
     * @return the found {@link CommandDefinition}.
     */
    public CommandDefinition findCommand(String path) {
        return this.commandDefinition.stream()
                .filter(commandDefinition -> commandDefinition.path().equals(path))
                .findFirst()
                .orElseThrow();
    }

    /**
     * Represents a definition of a command with its path, navigation option, and parameters.
     *
     * @param path           the path of the command.
     * @param withNavigation whether the command includes navigation.
     * @param parameters     the list of parameters associated with the command.
     */
    public record CommandDefinition(
            String path,
            boolean withNavigation,
            List<Parameter> parameters) {

        /**
         * Represents a parameter of a command.
         *
         * @param key           the key that identifies the parameter.
         * @param prompt        the prompt text for the parameter.
         * @param dynamicPrompt whether a dynamic prompt should be provided
         * @param required      whether the parameter is mandatory.
         */
        public record Parameter(
                String key,
                String prompt,
                boolean dynamicPrompt,
                boolean required) {
        }
    }
}
