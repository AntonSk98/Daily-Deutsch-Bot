package com.ansk.development.learngermanwithansk98.service.model;

import com.ansk.development.learngermanwithansk98.service.impl.AbstractCommandService;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.function.BiConsumer;

/**
 * Abstract command model.
 * It encapsulates common fields and methods that are required by {@link AbstractCommandService}.
 * @param <T> concrete command model
 */
public abstract class AbstractCommandModel<T> {

    private Iterator<String> paramIterator;

    public abstract LinkedHashMap<String, BiConsumer<T, String>> getMapping();

    public abstract T addMapping(String key, BiConsumer<T, String> mapping);

    public abstract void append(String awaitingKey, String input);

    public Iterator<String> getParamIterator() {
        if (paramIterator == null) {
            paramIterator = getMapping().keySet().iterator();
        }

        return paramIterator;
    }
}
