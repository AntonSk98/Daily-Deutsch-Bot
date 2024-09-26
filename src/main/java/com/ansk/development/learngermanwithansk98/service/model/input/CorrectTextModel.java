package com.ansk.development.learngermanwithansk98.service.model.input;

/**
 * Model that contains input parameters in order to create a text with its corrected version.
 *
 * @author Anton Skripin
 */
public class CorrectTextModel extends AbstractCommandModel<CorrectTextModel> implements IConfirmationModel {
    private String topic;
    private String textWithCorrections;
    private final CommandConfirmationModel confirmationModel = new CommandConfirmationModel();

    /**
     * Getter for {@link #topic}.
     *
     * @return {@link #topic}
     */
    public String getTopic() {
        return topic;
    }

    /**
     * Setter for {@link #topic}.
     *
     * @param topic {@link #topic}
     */
    public void setTopic(String topic) {
        this.topic = topic;
    }

    /**
     * Getter for {@link #textWithCorrections}.
     *
     * @return {@link #textWithCorrections}
     */
    public String getTextWithCorrections() {
        return textWithCorrections;
    }

    /**
     * Setter for {@link #textWithCorrections}.
     *
     * @param textWithCorrections {@link #textWithCorrections}
     */
    public void setTextWithCorrections(String textWithCorrections) {
        this.textWithCorrections = textWithCorrections;
    }

    @Override
    public AbstractCommandModel<CorrectTextModel> init() {
        return new CorrectTextModel();
    }

    @Override
    public void parseValue(String value) {
        this.confirmationModel.parseValue(value);
    }

    @Override
    public boolean shouldDo() {
        return confirmationModel.shouldDo();
    }
}
