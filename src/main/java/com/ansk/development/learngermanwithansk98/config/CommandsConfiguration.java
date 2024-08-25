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
@Configuration
@ConfigurationProperties(prefix = "commands")
public class CommandsConfiguration {
    private List<CommandDefinition> commandDefinition;

    public List<CommandDefinition> getCommandDefinition() {
        return commandDefinition;
    }

    public void setCommandDefinition(List<CommandDefinition> command) {
        this.commandDefinition = command;
    }

    public String findPrompt(String path, String key) {
        return findCommand(path)
                .getParameters()
                .stream()
                .filter(parameter -> parameter.key.equals(key))
                .findFirst()
                .orElseThrow()
                .prompt;
    }

    public CommandDefinition findCommand(String path) {
        return this.commandDefinition.stream().filter(commandDefinition -> commandDefinition.getPath().equals(path))
                .findFirst()
                .orElseThrow();
    }

    public static class CommandDefinition {
        private String path;
        private boolean withNavigation;
        private List<Parameter> parameters;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public List<Parameter> getParameters() {
            return parameters;
        }

        public void setParameters(List<Parameter> parameters) {
            this.parameters = parameters;
        }

        public boolean isWithNavigation() {
            return withNavigation;
        }

        public void setWithNavigation(boolean withNavigation) {
            this.withNavigation = withNavigation;
        }

        public static class Parameter {
            private String key;
            private String prompt;

            public String getKey() {
                return key;
            }

            public void setKey(String key) {
                this.key = key;
            }

            public String getPrompt() {
                return prompt;
            }

            public void setPrompt(String prompt) {
                this.prompt = prompt;
            }
        }
    }
}
