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

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTextWithCorrections() {
        return textWithCorrections;
    }

    public void setTextWithCorrections(String correctedExercise) {
        this.textWithCorrections = correctedExercise;
    }

    public boolean shouldPublish() {
        return shouldPublish;
    }

    public void setShouldPublish(boolean shouldPublish) {
        this.shouldPublish = shouldPublish;
    }

    @Override
    public AbstractCommandModel<CorrectTextModel> init() {
        return new CorrectTextModel();
    }
}
