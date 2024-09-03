package com.ansk.development.learngermanwithansk98.service.model.input;

/**
 * Model for a word that is to be deleted.
 *
 * @author Anton Skripin
 */
public class ToBeDeletedWord extends AbstractCommandModel<ToBeDeletedWord> {
    private String wordReference;

    public AbstractCommandModel<ToBeDeletedWord> init() {
        return new ToBeDeletedWord();
    }

    public String getWordReference() {
        return wordReference;
    }

    public void setWordReference(String wordReference) {
        this.wordReference = wordReference;
    }
}
