package com.ansk.development.learngermanwithansk98.service.model.input;

import static com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel.Properties.TRANSCRIPTION;

/**
 * Model to edit listening exercise.
 *
 * @author Anton Skripin
 */
public class EditListeningExerciseModel extends AbstractCommandModel<EditListeningExerciseModel> {

  private String editedTranscription;

  /**
   * Getter for {@link #editedTranscription}.
   *
   * @return {@link #editedTranscription}
   */
  public String getEditedTranscription() {
    return editedTranscription;
  }

  /**
   * Setter for {@link #editedTranscription}.
   *
   * @param editedTranscription {@link #editedTranscription}
   */
  public void setEditedTranscription(String editedTranscription) {
    this.editedTranscription = editedTranscription;
  }

  @Override
  public AbstractCommandModel<EditListeningExerciseModel> defineMapping() {
    return this.addMapping(TRANSCRIPTION, EditListeningExerciseModel::setEditedTranscription);
  }
}
