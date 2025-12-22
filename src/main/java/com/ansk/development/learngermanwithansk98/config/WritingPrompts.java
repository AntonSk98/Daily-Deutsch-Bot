package com.ansk.development.learngermanwithansk98.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Prompts to generate writing exercise.
 *
 * @param writingExample the prompt used to generate a writing example on a given topic.
 * @author Anton Skripin
 */
@ConfigurationProperties("ai.writing.prompts")
public record WritingPrompts(String writingExample) {}
