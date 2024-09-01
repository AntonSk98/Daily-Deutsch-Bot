package com.ansk.development.learngermanwithansk98.service.model.input;

import com.ansk.development.learngermanwithansk98.service.impl.command.AbstractCommandService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.function.BiConsumer;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

/**
 * Abstract command model.
 * It encapsulates common fields and methods that are required by {@link AbstractCommandService}.
 *
 * @param <T> concrete command model
 */
public abstract class AbstractCommandModel<T> {

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
        Map<?,?> map = new ObjectMapper().setSerializationInclusion(NON_NULL).convertValue(this, Map.class);
        return map.containsKey(key);
    }
}
