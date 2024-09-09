package com.ansk.development.learngermanwithansk98.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Contains prompt templates used to generate listening exercises
 *
 * @param createListeningExercise prompt to create a listening exercise
 * @param textToParagraphs        prompt to split transcription into paragraphs
 */
@ConfigurationProperties("ai.listening.prompts")
public record ListeningPromptConfiguration(String createListeningExercise, String textToParagraphs) {
}
