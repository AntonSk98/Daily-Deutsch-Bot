package com.ansk.development.learngermanwithansk98.service.model.input;

/**
 * Model that contains input parameters in order to create a text with its corrected version.
 *
 * @author Anton Skripin
 */
public class CorrectingWritingExerciseModel extends AbstractCommandModel<CorrectingWritingExerciseModel> {
    private String topic;
    private String correctedExercise;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getCorrectedExercise() {
        return correctedExercise;
    }

    public void setCorrectedExercise(String correctedExercise) {
        this.correctedExercise = correctedExercise;
    }

    @Override
    public AbstractCommandModel<CorrectingWritingExerciseModel> init() {
        return new CorrectingWritingExerciseModel();
    }
}
