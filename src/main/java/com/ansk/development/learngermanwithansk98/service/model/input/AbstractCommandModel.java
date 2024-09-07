package com.ansk.development.learngermanwithansk98.service.model.input;

import com.ansk.development.learngermanwithansk98.service.impl.command.AbstractCommandService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itextpdf.kernel.pdf.filespec.PdfStringFS;

import java.util.LinkedHashMap;
import java.util.ListIterator;
import java.util.Map;
import java.util.function.BiConsumer;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Abstract command model.
 * It encapsulates common fields and methods that are required by {@link AbstractCommandService}.
 *
 * @param <T> concrete command model
 */
public abstract class AbstractCommandModel<T> {

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
    }

    @JsonIgnore
    private final LinkedHashMap<String, BiConsumer<T, String>> mapping = new LinkedHashMap<>();

    @JsonIgnore
    private ListIterator<String> paramIterator;

    public LinkedHashMap<String, BiConsumer<T, String>> getMapping() {
        return mapping;
    }

    public abstract AbstractCommandModel<T> init();

    public T addMapping(String key, BiConsumer<T, String> mapping) {
        this.mapping.put(key, mapping);
        return (T) this;
    }

    public void append(String awaitingKey, String input) {
        getMapping().get(awaitingKey).accept((T) this, input);
    }

    public ListIterator<String> getParamIterator() {
        if (paramIterator == null) {
            paramIterator = getMapping().keySet().stream().toList().listIterator();
        }

        return paramIterator;
    }

    public boolean isDefined(String key) {
        Map<?, ?> map = new ObjectMapper().setSerializationInclusion(NON_NULL).convertValue(this, Map.class);
        return map.containsKey(key);
    }

    public <T> T map(Class<T> clazz) {
        return clazz.cast(this);
    }
}
