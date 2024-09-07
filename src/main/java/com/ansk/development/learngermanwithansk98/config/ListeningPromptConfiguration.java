package com.ansk.development.learngermanwithansk98.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("ai.listening.prompts")
public record ListeningPromptConfiguration(String createListeningExercise, String textToParagraphs) {
}
