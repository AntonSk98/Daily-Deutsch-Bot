package com.ansk.development.learngermanwithansk98.service.model.input;

/**
 * Model to provide necessary parameters for creating a reading exercise from the given text.
 *
 * @author Anton Skripin
 */
public class ReadingExerciseWithTextModel extends AbstractCommandModel<ReadingExerciseWithTextModel> {
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public AbstractCommandModel<ReadingExerciseWithTextModel> init() {
        return new ReadingExerciseWithTextModel();
    }
}
