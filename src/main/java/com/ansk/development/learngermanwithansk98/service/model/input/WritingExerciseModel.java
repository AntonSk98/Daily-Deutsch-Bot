package com.ansk.development.learngermanwithansk98.service.model.input;

import static com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel.Properties.LEVEL;
import static com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel.Properties.TOPIC;

/**
 * Model for generating a writing exercise.
 *
 * @author Anton Skripin
 */
public class WritingExerciseModel extends AbstractCommandModel<WritingExerciseModel> {
  private String topic;
  private String level;

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

  @Override
  public AbstractCommandModel<WritingExerciseModel> defineMapping() {
    return this.addMapping(LEVEL, WritingExerciseModel::setLevel)
        .addMapping(TOPIC, WritingExerciseModel::setTopic);
  }
}
