package com.ansk.development.learngermanwithansk98.integration.openai;

import java.util.List;

/**
 * Encapsulates output response from OpenAI.
 * This record represents the structure of the response received from the OpenAI API.
 *
 * @author Anton Skripin
 */
public record Output(String id,
                     String model,
                     List<Choice> choices) {
}
