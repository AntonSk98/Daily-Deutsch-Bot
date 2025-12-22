package com.ansk.development.learngermanwithansk98.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Prompts to generate listening exercise.
 *
 * @param createListeningExercise prompt to create a listening exercise
 * @param textToParagraphs prompt to split transcription into paragraphs
 */
@ConfigurationProperties("ai.listening.prompts")
public record ListeningPrompts(String createListeningExercise, String textToParagraphs) {}
