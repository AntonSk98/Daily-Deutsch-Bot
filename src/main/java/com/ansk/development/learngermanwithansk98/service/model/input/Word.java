package com.ansk.development.learngermanwithansk98.service.model.input;

import org.apache.commons.lang3.StringUtils;

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
    private int frequency;
    private String example;
    private String exampleTranslation;

    /**
     * Creates a new {@link Word}.
     *
     * @param word        word
     * @param translation translation
     * @return new {@link Word}
     */
    public static Word of(String word, String translation) {
        Word w = new Word();
        w.setWord(word);
        w.setTranslation(translation);
        return w;
    }

    /**
     * Getter for {@link #word}.
     *
     * @return {@link #word}
     */
    public String getWord() {
        return word;
    }

    /**
     * Getter for {@link #translation}.
     *
     * @return {@link #translation}
     */
    public String getTranslation() {
        return translation;
    }

    /**
     * Getter for {@link #meaning}.
     *
     * @return {@link #meaning}
     */
    public String getMeaning() {
        return meaning;
    }

    /**
     * Getter for {@link #forms}.
     *
     * @return {@link #forms}
     */
    public String getForms() {
        return forms;
    }

    /**
     * Getter for {@link #frequency}.
     *
     * @return {@link #frequency}
     */
    public int getFrequency() {
        return frequency;
    }

    /**
     * Getter for {@link #example}.
     *
     * @return {@link #example}
     */
    public String getExample() {
        return example;
    }

    /**
     * Getter for {@link #exampleTranslation}.
     *
     * @return {@link #exampleTranslation}
     */
    public String getExampleTranslation() {
        return exampleTranslation;
    }

    /**
     * Setter for {@link #word}.
     *
     * @param word {@link #word}
     */
    public void setWord(String word) {
        this.word = word;
    }

    /**
     * Setter for {@link #translation}.
     *
     * @param translation {@link #translation}
     */
    public void setTranslation(String translation) {
        this.translation = translation;
    }

    /**
     * Setter for {@link #meaning}.
     *
     * @param meaning {@link #meaning}
     */
    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    /**
     * Setter for {@link #forms}.
     *
     * @param forms {@link #forms}
     */
    public void setForms(String forms) {
        this.forms = StringUtils.join(forms.split("\\."), " | ");
    }

    /**
     * Setter for {@link #frequency}.
     *
     * @param frequency {@link #frequency}
     */
    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    /**
     * Setter for {@link #example}.
     *
     * @param example {@link #example}
     */
    public void setExample(String example) {
        this.example = example;
    }

    /**
     * Setter for {@link #exampleTranslation}.
     *
     * @param exampleTranslation {@link #exampleTranslation}
     */
    public void setExampleTranslation(String exampleTranslation) {
        this.exampleTranslation = exampleTranslation;
    }

    @Override
    public AbstractCommandModel<Word> init() {
        return new Word();
    }
}
