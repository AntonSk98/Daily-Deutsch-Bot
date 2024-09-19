package com.ansk.development.learngermanwithansk98.service.model.input;

/**
 * Model that contains input parameters in order to create a text with its corrected version.
 *
 * @author Anton Skripin
 */
public class CorrectTextModel extends AbstractCommandModel<CorrectTextModel> {
    private String topic;
    private String textWithCorrections;
    boolean shouldPublish;

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

    /**
     * Getter for {@link #shouldPublish}.
     *
     * @return {@link #shouldPublish}
     */
    public boolean shouldPublish() {
        return shouldPublish;
    }

    /**
     * Setter for {@link #shouldPublish}.
     *
     * @param shouldPublish {@link #shouldPublish}
     */
    public void setShouldPublish(boolean shouldPublish) {
        this.shouldPublish = shouldPublish;
    }

    @Override
    public AbstractCommandModel<CorrectTextModel> init() {
        return new CorrectTextModel();
    }
}
