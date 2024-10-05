package com.ansk.development.learngermanwithansk98.integration.openai;

import com.ansk.development.learngermanwithansk98.config.OpenAIConfiguration;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Gateway to communicate with the OpenAI.
 *
 * @author Anton Skripin
 */
@Component
public class OpenAiClient {

    private static final String TEXT_REQUEST_URL = "https://api.openai.com/v1/chat/completions";
    private static final String TRANSCRIBE_AUDIO_URL = "https://api.openai.com/v1/audio/transcriptions";
    private static final String CREATE_SPEECH_URL = "https://api.openai.com/v1/audio/speech";

    private final RestTemplate restTemplate;
    private final OpenAIConfiguration openAIConfiguration;
    private final ObjectMapper objectMapper;

    /**
     * Constructor.
     *
     * @param openAIConfiguration See {@link OpenAIConfiguration}
     * @param objectMapper        See {@link ObjectMapper}
     */
    public OpenAiClient(OpenAIConfiguration openAIConfiguration,
                        ObjectMapper objectMapper) {
        this.openAIConfiguration = openAIConfiguration;
        this.objectMapper = objectMapper;
        this.restTemplate = new RestTemplate();
    }

    /**
     * Sends a request to the OpenAI API to generate a response based on the provided prompt.
     *
     * @param prompt     the input prompt for generating a text completion.
     * @param clazz      the class type to which the response should be mapped.
     * @param <Response> the type of the response.
     * @return the response mapped to the specified class type.
     * @throws RuntimeException if the response cannot be converted to the specified class type.
     */
    public <Response> Response sendRequest(String prompt, Class<Response> clazz) {
        HttpHeaders headers = headersWithAuth();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Request request = new Request(
                openAIConfiguration.textBasedModelConfiguration().model(),
                List.of(new Message(openAIConfiguration.textBasedModelConfiguration().role(), prompt)),
                openAIConfiguration.textBasedModelConfiguration().temperature(),
                openAIConfiguration.textBasedModelConfiguration().maxTokens(),
                openAIConfiguration.textBasedModelConfiguration().frequencyPenalty(),
                openAIConfiguration.textBasedModelConfiguration().presencePenalty(),
                new ResponseFormat(openAIConfiguration.textBasedModelConfiguration().responseFormat())
        );

        HttpEntity<Request> entity = new HttpEntity<>(request, headers);

        Output output = restTemplate.exchange(TEXT_REQUEST_URL, HttpMethod.POST, entity, Output.class).getBody();

        String jsonResponse = Objects.requireNonNull(output)
                .choices()
                .stream()
                .findFirst()
                .map(Choice::message)
                .map(Message::content)
                .orElseThrow();

        try {
            return objectMapper.readValue(jsonResponse, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert AI response to clazz: " + clazz + " Output: " + jsonResponse, e);
        }
    }

    /**
     * Transcribes audio by sending it to the OpenAI API and returns the transcription text.
     *
     * @param audioStream the input stream of the audio to be transcribed.
     * @return the transcription text of the audio.
     * @throws RuntimeException if an error occurs while transcribing the audio.
     */
    public String transcribeAudio(InputStream audioStream) {
        HttpHeaders headers = headersWithAuth();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = transcribeAudioRequestBody(audioStream, headers);
        ResponseEntity<Map<String, String>> response = restTemplate.exchange(TRANSCRIBE_AUDIO_URL,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<>() {
                }
        );
        return Optional.ofNullable(response.getBody()).map(transcription -> transcription.get("text")).orElseThrow();
    }

    /**
     * Creates a speech from the submitted text.
     *
     * @param text text that is to be converted to speech
     * @return voiced text as byte array
     */
    public byte[] createSpeech(String text) {
        HttpHeaders headers = headersWithAuth();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, String>> requestBody = createSpeechRequestBody(headers, text);
        ResponseEntity<byte[]> response = restTemplate.exchange(
                CREATE_SPEECH_URL,
                HttpMethod.POST,
                requestBody,
                byte[].class
        );

        return Optional.ofNullable(response.getBody()).orElseThrow();
    }

    private HttpHeaders headersWithAuth() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(openAIConfiguration.token());
        return headers;
    }

    private static HttpEntity<MultiValueMap<String, Object>> transcribeAudioRequestBody(InputStream audioStream, HttpHeaders headers) {
        try {
            ByteArrayResource byteArrayResource = new ByteArrayResource(audioStream.readAllBytes()) {
                @Override
                public String getFilename() {
                    return "audio.mp3";
                }
            };

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", byteArrayResource);
            body.add("model", "whisper-1");
            body.add("language", "de");

            return new HttpEntity<>(body, headers);
        } catch (IOException e) {
            throw new RuntimeException("Error occurred while creating an audio stream that is to be transcribed", e);
        }
    }

    private HttpEntity<Map<String, String>> createSpeechRequestBody(HttpHeaders headers, String text) {
        Map<String, String> body = new HashMap<>();
        body.put("model", "tts-1");
        body.put("input", text);
        body.put("voice", "shimmer");

        return new HttpEntity<>(body, headers);
    }
}


