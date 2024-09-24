package com.ansk.development.learngermanwithansk98.service.model.input;

/**
 * Model to provide necessary parameters for creating a reading exercise from the given text.
 *
 * @author Anton Skripin
 */
public class ReadingExerciseWithTextModel extends AbstractCommandModel<ReadingExerciseWithTextModel> implements IConfirmationModel {
    private String text;
    private boolean shouldRephrase;
    private final CommandConfirmationModel confirmationModel = new CommandConfirmationModel();

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

    @Override
    public AbstractCommandModel<ReadingExerciseWithTextModel> init() {
        return new ReadingExerciseWithTextModel();
    }

    @Override
    public void parseValue(String value) {
        confirmationModel.parseValue(value);
    }

    @Override
    public boolean shouldDo() {
        return confirmationModel.shouldDo();
    }
}
