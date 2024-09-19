package com.ansk.development.learngermanwithansk98.service.model.input;

/**
 * Model to provide necessary parameters for creating a reading exercise from the given text.
 *
 * @author Anton Skripin
 */
public class ReadingExerciseWithTextModel extends AbstractCommandModel<ReadingExerciseWithTextModel> {
    private String text;
    private boolean shouldRephrase;

    /**
     * Getter for {@link #text}.
     *
     * @return {@link #text}
     */
    public String getText() {
        return text;
    }

    /**
     * Setter for {@link #text}.
     *
     * @param text {@link #text}
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Getter for {@link #shouldRephrase}.
     *
     * @return {@link #shouldRephrase}
     */
    public boolean shouldRephrase() {
        return this.shouldRephrase;
    }

    /**
     * Setter for {@link #shouldRephrase}.
     *
     * @param shouldRephrase {@link #shouldRephrase}
     */
    public void shouldRephrase(boolean shouldRephrase) {
        this.shouldRephrase = shouldRephrase;
    }

    @Override
    public AbstractCommandModel<ReadingExerciseWithTextModel> init() {
        return new ReadingExerciseWithTextModel();
    }
}
