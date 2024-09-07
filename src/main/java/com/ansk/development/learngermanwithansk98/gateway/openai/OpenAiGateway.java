package com.ansk.development.learngermanwithansk98.gateway.openai;

import com.ansk.development.learngermanwithansk98.config.OpenAIConfiguration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Gateway to communicate with the OpenAI.
 *
 * @author Anton Skripin
 */
@Component
public class OpenAiGateway {

    private static final String TEXT_REQUEST_URL = "https://api.openai.com/v1/chat/completions";
    private static final String AUDIO_URL = "https://api.openai.com/v1/audio/transcriptions";

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
        HttpHeaders headers = headersWithAuth();
        headers.setContentType(MediaType.APPLICATION_JSON);

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

        Output output = restTemplate.exchange(TEXT_REQUEST_URL, HttpMethod.POST, entity, Output.class).getBody();

        String jsonResponse = output.choices().stream().findFirst().map(Choice::message).map(Message::content).orElseThrow();

        try {
            return objectMapper.readValue(jsonResponse, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert AI response to clazz: " + clazz + " Output: " + jsonResponse, e);
        }
    }

    public String transcribeAudio(InputStream audioStream) {
        HttpHeaders headers = headersWithAuth();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new InputStreamResource(audioStream));
        body.add("model", "whisper-1");
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<Map<String, String>> response = restTemplate.exchange(AUDIO_URL, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<>() {});
        return Optional.ofNullable(response.getBody()).map(transcription -> transcription.get("text")).orElseThrow();
    }

    private HttpHeaders headersWithAuth() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(openAIConfiguration.token());
        return headers;
    }
}


