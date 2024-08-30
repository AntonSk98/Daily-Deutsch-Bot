package com.ansk.development.learngermanwithansk98.gateway.openai;

import com.ansk.development.learngermanwithansk98.config.OpenAIConfiguration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

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

    public OpenAiGateway(OpenAIConfiguration openAIConfiguration) {
        this.openAIConfiguration = openAIConfiguration;
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
                openAIConfiguration.tempearature(),
                openAIConfiguration.maxTokens(),
                openAIConfiguration.frequencyPenalty(),
                openAIConfiguration.presencePenalty(),
                new ResponseFormat(openAIConfiguration.responseFormat())
        );

        HttpEntity<Input> entity = new HttpEntity<>(input, headers);

        return restTemplate.exchange(URL, HttpMethod.POST, entity, clazz).getBody();
    }
}


