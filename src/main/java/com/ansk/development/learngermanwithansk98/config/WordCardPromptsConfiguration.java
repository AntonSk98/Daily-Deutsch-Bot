package com.ansk.development.learngermanwithansk98.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration for creating words cards.
 *
 * @param autoWordDefinition prompt to auto-define a word
 * @author Anton Skripin
 */
@ConfigurationProperties("ai.vocabulary.prompts")
public record WordCardPromptsConfiguration(String autoWordDefinition) {
}
