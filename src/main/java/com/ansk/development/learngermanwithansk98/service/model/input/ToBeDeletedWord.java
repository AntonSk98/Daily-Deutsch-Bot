package com.ansk.development.learngermanwithansk98.service.model.input;

/**
 * Model for a word that is to be deleted from cache.
 *
 * @author Anton Skripin
 */
public class ToBeDeletedWord extends AbstractCommandModel<ToBeDeletedWord> {
    private String wordReference;

    @Override
    public AbstractCommandModel<ToBeDeletedWord> init() {
        return new ToBeDeletedWord();
    }

    /**
     * Getter for {@link #wordReference}.
     *
     * @return {@link #wordReference}
     */
    public String getWordReference() {
        return wordReference;
    }

    /**
     * Setter for {@link #wordReference}.
     *
     * @param wordReference {@link #wordReference}
     */
    public void setWordReference(String wordReference) {
        this.wordReference = wordReference;
    }
}
