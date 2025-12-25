package com.ansk.development.learngermanwithansk98.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Prompts to create a flashcard.
 *
 * @param autoWordDefinition the prompt used to auto-define words for flashcards.
 * @author Anton Skripin
 */
@ConfigurationProperties("ai.flashcard.prompts")
public record FlashcardPrompts(String autoWordDefinition) {}
