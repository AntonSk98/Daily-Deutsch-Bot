package com.ansk.development.learngermanwithansk98.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("ai.vocabulary.prompts")
public record WordCardPromptsConfiguration(String autoWordDefinition) {
}
