package com.ansk.development.learngermanwithansk98.service.model.input;

/**
 * Model to provide required parameters required to generate a reading exercise.
 *
 * @author Anton Skripin
 */
public class ReadingExerciseModel extends AbstractCommandModel<ReadingExerciseModel> {
    private String level;
    private String topic;


    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public AbstractCommandModel<ReadingExerciseModel> init() {
        return new ReadingExerciseModel();
    }
}
