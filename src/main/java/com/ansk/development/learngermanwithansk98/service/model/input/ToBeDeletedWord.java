package com.ansk.development.learngermanwithansk98.service.model;

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

    public static ToBeDeletedWord map(AbstractCommandModel<?> model) {
        if (model instanceof ToBeDeletedWord toBeDeletedWord) {
            return toBeDeletedWord;
        }
        throw new IllegalArgumentException("Model is not an instance of class Word: " + model.getClass().getSimpleName());
    }
}
