package com.ansk.development.learngermanwithansk98.service.model;

/**
 * Implementation of {@link AbstractCommandModel} for a word.
 *
 * @author Anton Skripin
 */
public class Word extends AbstractCommandModel<Word> {
    private String word;
    private String translation;
    private String meaning;
    private String forms;
    private String frequency;
    private String example;
    private String exampleTranslation;

    public static Word toWord(AbstractCommandModel<?> model) {
        if (model instanceof Word word) {
            return word;
        }

        throw new IllegalArgumentException("Model is not an instance of class Word: " + model.getClass().getSimpleName());
    }

    public String getWord() {
        return word;
    }

    public String getTranslation() {
        return translation;
    }

    public String getMeaning() {
        return meaning;
    }

    public String getForms() {
        return forms;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getExample() {
        return example;
    }

    public String getExampleTranslation() {
        return exampleTranslation;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public void setForms(String forms) {
        this.forms = forms;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public void setExampleTranslation(String exampleTranslation) {
        this.exampleTranslation = exampleTranslation;
    }

    @Override
    public AbstractCommandModel<Word> init() {
        return new Word();
    }
}
