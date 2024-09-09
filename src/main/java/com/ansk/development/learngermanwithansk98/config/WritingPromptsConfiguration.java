package com.ansk.development.learngermanwithansk98.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration for creating writing exercises.
 * Contains a prompt for generating a writing example based on a given topic.
 *
 * @param writingExample the prompt used to generate a writing example on a given topic.
 * @author Anton Skripin
 */
@ConfigurationProperties("ai.writing.prompts")
public record WritingPromptsConfiguration(String writingExample) {
}
