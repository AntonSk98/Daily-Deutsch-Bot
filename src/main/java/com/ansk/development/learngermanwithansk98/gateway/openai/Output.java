package com.ansk.development.learngermanwithansk98.gateway.openai;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Encapsulates output response from OpenAI.
 *
 * @author Anton Skripin
 */
public record Output(String id,
                     String model,
                     List<Choice> choices) {
}
