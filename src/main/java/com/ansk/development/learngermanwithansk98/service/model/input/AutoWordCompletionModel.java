package com.ansk.development.learngermanwithansk98.service.model.input;

/**
 * The {@code AutoWordCompletionModel} is a model class designed to facilitate automatic completion
 * of word-related data for learning purposes. This class is used as an input model where the user
 * specifies a word, and the system automatically fills in additional information required for
 * learning or processing that word.
 *
 * @author Anton Skripin
 */
public class AutoWordCompletionModel extends AbstractCommandModel<AutoWordCompletionModel> {

    private String word;

    /**
     * Getter for {@link #word}.
     *
     * @return {@link #word}
     */
    public String getWord() {
        return word;
    }

    /**
     * Setter for {@link #word}.
     *
     * @param word {@link #word}
     */
    public void setWord(String word) {
        this.word = word;
    }

    @Override
    public AbstractCommandModel<AutoWordCompletionModel> init() {
        return new AutoWordCompletionModel();
    }
}
