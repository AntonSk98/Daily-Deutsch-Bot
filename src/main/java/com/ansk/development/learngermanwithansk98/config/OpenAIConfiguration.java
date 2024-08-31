package com.ansk.development.learngermanwithansk98.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("ai.params")
public record OpenAIConfiguration(String token,
                                  String model,
                                  String role,
                                  float temperature,
                                  long maxTokens,
                                  float frequencyPenalty,
                                  float presencePenalty,
                                  String responseFormat) {
}
