package com.ansk.development.learngermanwithansk98.integration.openai;

/**
 * Encapsulates message parameters to submit to OpenAI.
 *
 * @author Anton Skripin
 */
public record Message(String role, String content) {
}
