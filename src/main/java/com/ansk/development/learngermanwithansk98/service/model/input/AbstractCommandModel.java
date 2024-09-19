package com.ansk.development.learngermanwithansk98.service.model.input;

import com.ansk.development.learngermanwithansk98.service.impl.command.AbstractCommandProcessor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.function.BiConsumer;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Abstract base class for command models.
 * It encapsulates common fields and methods that are required by {@link AbstractCommandProcessor}.
 * <p>
 * The class supports mappings definition between input parameter keys and the associated object setter that extends the abstract model.
 *
 * @param <T> concrete command model extending this abstract model
 */
public abstract class AbstractCommandModel<T> {

    /**
     * Defines constants representing common property names used in command models.
     * These properties are keys that are typically associated with their respective values in command models.
     */
    public static final class Properties {
        public static final String WORD = "word";
        public static final String TRANSLATION = "translation";
        public static final String MEANING = "meaning";
        public static final String FORMS = "forms";
        public static final String FREQUENCY = "frequency";
        public static final String EXAMPLE = "example";
        public static final String EXAMPLE_TRANSLATION = "example_translation";
        public static final String WORD_REFERENCE = "word_reference";
        public static final String LEVEL = "level";
        public static final String TOPIC = "topic";
        public static final String TEXT = "text";
        public static final String AUDIO = "audio";
        public static final String SHOULD_REPHRASE_TEXT = "should_rephrase_text";
        public static final String TRANSCRIPTION = "transcription";
        public static final String CORRECTED_TEXT = "corrected_text";
        public static final String SHOULD_PUBLISH_TEXT_AND_CORRECTION = "should_publish_text_and_correction";
        public static final String APPROVE_PROMPT = "+";

    }

    @JsonIgnore
    private final LinkedHashMap<String, BiConsumer<T, String>> mapping = new LinkedHashMap<>();

    @JsonIgnore
    private ListIterator<String> paramIterator;

    /**
     * Retrieves the mapping of input parameter keys to processing actions.
     *
     * @return a {@link LinkedHashMap} that contains the mapping of keys to {@link BiConsumer} actions
     */
    public LinkedHashMap<String, BiConsumer<T, String>> getMapping() {
        return mapping;
    }

    /**
     * Initializes the concrete command model.
     * Subclasses must implement this method to set up any necessary mappings or configurations specific to the command.
     *
     * @return the initialized instance of the concrete command model
     */
    public abstract AbstractCommandModel<T> init();

    /**
     * Adds a new key-to-action mapping for the command model.
     *
     * @param key     the input parameter key to associate with the mapping
     * @param mapping the {@link BiConsumer} that defines the action to perform for the given key
     * @return the updated instance of the concrete command model
     */
    public T addMapping(String key, BiConsumer<T, String> mapping) {
        this.mapping.put(key, mapping);
        return (T) this;
    }

    /**
     * Processes input by applying the action associated with the given key.
     *
     * @param awaitingKey the input parameter key whose associated action should be executed
     * @param input       the input value to pass to the action
     */
    public void append(String awaitingKey, String input) {
        getMapping().get(awaitingKey).accept((T) this, input);
    }

    /**
     * Retrieves an iterator over the parameter keys in the command model, allowing for sequential processing.
     *
     * @return a {@link ListIterator} over the keys in the mapping
     */
    public ListIterator<String> getParamIterator() {
        if (paramIterator == null) {
            paramIterator = getMapping().keySet().stream().toList().listIterator();
        }

        return paramIterator;
    }

    /**
     * Checks if the value is already set for the given key in the current command model.
     *
     * @param key the input parameter key to check
     * @return {@code true} if the key is defined, {@code false} otherwise
     */
    public boolean isDefined(String key) {
        Map<?, ?> map = new ObjectMapper().setSerializationInclusion(NON_NULL).convertValue(this, Map.class);
        return map.containsKey(key);
    }

    /**
     * Casts the current instance to the specified class type.
     *
     * @param clazz clazz the class type to cast to
     * @param <T>   target
     * @return mapped model
     */
    public <T> T map(Class<T> clazz) {
        return clazz.cast(this);
    }
}
