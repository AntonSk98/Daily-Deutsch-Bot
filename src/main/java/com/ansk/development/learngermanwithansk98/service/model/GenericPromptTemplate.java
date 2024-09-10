package com.ansk.development.learngermanwithansk98.service.model;

import org.apache.commons.lang3.StringUtils;

/**
 * Encapsulates a template with generic placeholders and provides ways to replace them with particular variables.
 *
 * @author Anton Skripin
 */
public class GenericPromptTemplate {

    private static final String VARIABLE_START = "{";
    private static final String VARIABLE_END = "}";

    private String template;

    /**
     * Constructor.
     *
     * @param template template with placeholders
     */
    public GenericPromptTemplate(String template) {
        this.template = template;
    }

    /**
     * Replaces a variable in a placeholder with provided value.
     *
     * @param variable variable placeholder
     * @param value    concrete value
     * @return template
     */
    public GenericPromptTemplate resolveVariable(String variable, String value) {
        template = StringUtils.replace(template, VARIABLE_START + variable + VARIABLE_END, value);
        return this;
    }

    /**
     * Gets resolved prompt out of the temlate.
     *
     * @return prompt
     */
    public String getPrompt() {
        return template;
    }
}
