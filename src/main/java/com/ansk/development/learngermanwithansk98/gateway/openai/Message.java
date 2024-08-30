package com.ansk.development.learngermanwithansk98.gateway.openai;

/**
 * Encapsulates message parameters to submit to OpenAI.
 *
 * @author Anton Skripin
 */
public record Message(String role, String content) {
}
