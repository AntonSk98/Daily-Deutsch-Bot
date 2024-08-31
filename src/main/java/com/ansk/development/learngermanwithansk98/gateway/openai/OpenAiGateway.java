package com.ansk.development.learngermanwithansk98.gateway.openai;

import com.ansk.development.learngermanwithansk98.config.OpenAIConfiguration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * Gateway to communicate with the OpenAI.
 *
 * @author Anton Skripin
 */
@Component
public class OpenAiGateway {

    private static final String URL = "https://api.openai.com/v1/chat/completions";

    private final RestTemplate restTemplate;
    private final OpenAIConfiguration openAIConfiguration;
    private final ObjectMapper objectMapper;

    public OpenAiGateway(OpenAIConfiguration openAIConfiguration,
                         ObjectMapper objectMapper) {
        this.openAIConfiguration = openAIConfiguration;
        this.objectMapper = objectMapper;
        this.restTemplate = new RestTemplate();
    }

    public <Response> Response sendRequest(String prompt, Class<Response> clazz) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAIConfiguration.token());

        Input input = new Input(
                openAIConfiguration.model(),
                List.of(
                        new Message(openAIConfiguration.role(), prompt)
                ),
                openAIConfiguration.temperature(),
                openAIConfiguration.maxTokens(),
                openAIConfiguration.frequencyPenalty(),
                openAIConfiguration.presencePenalty(),
                new ResponseFormat(openAIConfiguration.responseFormat())
        );

        HttpEntity<Input> entity = new HttpEntity<>(input, headers);

        Output output =  restTemplate.exchange(URL, HttpMethod.POST, entity, Output.class).getBody();

        String jsonResponse = output.choices().stream().findFirst().map(Choice::message).map(Message::content).orElseThrow();

        try {
            return objectMapper.readValue(jsonResponse, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert AI response to clazz: " + clazz + " Output: " + jsonResponse, e);
        }
    }
}


