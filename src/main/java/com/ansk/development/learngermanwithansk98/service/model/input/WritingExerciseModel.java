package com.ansk.development.learngermanwithansk98.service.model.input;

/**
 * Model for generating a writing exercise.
 *
 * @author Anton Skripin
 */
public class WritingExerciseModel extends AbstractCommandModel<WritingExerciseModel> {
    private String topic;
    private String level;

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public AbstractCommandModel<WritingExerciseModel> init() {
        return new WritingExerciseModel();
    }
}
