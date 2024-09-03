package com.ansk.development.learngermanwithansk98.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration for creating a reading exercise.
 *
 * @param generateText          prompt to generate a text
 * @param rephraseText          prompt to rephrase a given text
 * @param createReadingExercise prompt to create a reading exercise with answers
 * @param textToParagraphs      prompt to split a text into paragraphs
 * @author Anton Skripin
 */
@ConfigurationProperties("ai.reading.prompts")
public record ReadingPromptsConfiguration(
        String generateText,
        String rephraseText,
        String createReadingExercise,
        String textToParagraphs
) {
}
