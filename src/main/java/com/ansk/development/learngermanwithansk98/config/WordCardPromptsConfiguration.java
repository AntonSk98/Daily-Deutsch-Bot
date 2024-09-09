package com.ansk.development.learngermanwithansk98.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration for creating word cards.
 * A word card is generated using a prompt to automatically define words.
 *
 * @param autoWordDefinition the prompt used to auto-define words for the word card.
 * @author Anton Skripin
 */
@ConfigurationProperties("ai.vocabulary.prompts")
public record WordCardPromptsConfiguration(String autoWordDefinition) {
}
