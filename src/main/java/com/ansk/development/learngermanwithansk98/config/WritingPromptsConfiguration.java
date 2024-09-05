package com.ansk.development.learngermanwithansk98.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration for creating writing exercise.
 *
 * @param writingExample     prompt to generate a writing example on a given topic
 * @param writingCorrections prompt to correct a provided text
 * @author Anton Skripin
 */
@ConfigurationProperties("ai.writing.prompts")
public record WritingPromptsConfiguration(String writingExample, String writingCorrections) {
}
