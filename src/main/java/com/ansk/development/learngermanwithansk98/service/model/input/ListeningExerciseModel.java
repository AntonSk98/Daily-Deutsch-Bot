package com.ansk.development.learngermanwithansk98.service.model.input;

import static com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel.Properties.AUDIO;

/**
 * A model representing a listening exercise input.
 *
 * @author Anton Skripin
 */
public class ListeningExerciseModel extends AbstractCommandModel<ListeningExerciseModel> {
  private String audioId;

  /**
   * Getter for {@link #audioId}.
   *
   * @return {@link #audioId}
   */
  public String getAudio() {
    return audioId;
  }

  /**
   * Setter for {@link #audioId}.
   *
   * @param audio {@link #audioId}
   */
  public void setAudio(String audio) {
    this.audioId = audio;
  }

  @Override
  public AbstractCommandModel<ListeningExerciseModel> defineMapping() {
    return this.addMapping(AUDIO, ListeningExerciseModel::setAudio);
  }
}
