package com.ansk.development.learngermanwithansk98.integration.openai;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


/**
 * Encapsulates parameters required to send a request to OpenAI.
 *
 * @author Anton Skripin
 */
public record Request(String model,
                      List<Message> messages,
                      float temperature,
                      @JsonProperty("max_tokens") long maxTokens,
                      @JsonProperty("frequency_penalty") float frequencyPenalty,
                      @JsonProperty("presence_penalty") float presencePenalty,
                      @JsonProperty("response_format") ResponseFormat responseFormat) {

}


