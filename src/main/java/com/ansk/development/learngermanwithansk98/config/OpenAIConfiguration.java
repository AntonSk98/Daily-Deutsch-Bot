package com.ansk.development.learngermanwithansk98.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for OpenAI API interactions.
 * <p>
 * Contains the parameters for both text-based and audio-based models,
 * allowing configuration of different model types based on the task.
 * </p>
 *
 * @param token                        the authentication token for accessing the OpenAI API.
 * @param textBasedModelConfiguration  the configuration parameters for text-based models.
 * @param audioBasedModelConfiguration the configuration parameters for audio-based models.
 */
@ConfigurationProperties("ai.params")
public record OpenAIConfiguration(String token,
                                  TextBasedModelConfiguration textBasedModelConfiguration,
                                  AudioBasedModelConfiguration audioBasedModelConfiguration) {

    /**
     * Configuration properties for OpenAI text-based models.
     *
     * @param model            the OpenAI text-based model to be used for generating responses.
     * @param role             the role that guides the assistant's behavior.
     * @param temperature      the sampling temperature to control the randomness of responses.
     * @param maxTokens        the maximum number of tokens in the response.
     * @param frequencyPenalty the penalty for repeating words or phrases in the response.
     * @param presencePenalty  the penalty for introducing new topics in the conversation.
     * @param responseFormat   the format in which the response will be returned (e.g., JSON).
     */
    public record TextBasedModelConfiguration(String model,
                                              String role,
                                              float temperature,
                                              long maxTokens,
                                              float frequencyPenalty,
                                              float presencePenalty,
                                              String responseFormat) {
    }

    /**
     * Configuration properties for OpenAI audio-based models.
     *
     * @param model    the OpenAI audio-based model to be used for processing audio.
     * @param language the language configuration for the audio model.
     */
    public record AudioBasedModelConfiguration(String model, String language) {

    }
}
