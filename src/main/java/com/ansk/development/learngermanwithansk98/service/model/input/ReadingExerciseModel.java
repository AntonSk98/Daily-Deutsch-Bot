package com.ansk.development.learngermanwithansk98.service.model.input;

/**
 * Model to provide required parameters required to generate a reading exercise.
 *
 * @author Anton Skripin
 */
public class ReadingExerciseModel extends AbstractCommandModel<ReadingExerciseModel> {
    private String level;
    private String topic;


    /**
     * Getter for {@link #level}.
     *
     * @return {@link #level}
     */
    public String getLevel() {
        return level;
    }

    /**
     * Setter for {@link #level}.
     *
     * @param level {@link #level}
     */
    public void setLevel(String level) {
        this.level = level;
    }

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

    @Override
    public AbstractCommandModel<ReadingExerciseModel> init() {
        return new ReadingExerciseModel();
    }
}
