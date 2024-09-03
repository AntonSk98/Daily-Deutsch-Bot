package com.ansk.development.learngermanwithansk98.service.model;

import org.apache.commons.lang3.StringUtils;

/**
 * Encapsulates a template with generic placeholders and provides means to replace them.
 *
 * @author Anton Skripin
 */
public class GenericPromptTemplate {

    private static final String VARIABLE_START = "{";
    private static final String VARIABLE_END = "}";

    private String template;

    public GenericPromptTemplate(String template) {
        this.template = template;
    }

    public GenericPromptTemplate resolveVariable(String variable, String value) {
        template = StringUtils.replace(template, VARIABLE_START + variable + VARIABLE_END, value);
        return this;
    }

    public String getPrompt() {
        return template;
    }
}
