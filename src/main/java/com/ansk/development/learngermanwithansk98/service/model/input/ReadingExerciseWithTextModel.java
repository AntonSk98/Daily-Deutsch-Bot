package com.ansk.development.learngermanwithansk98.service.model.input;

import static com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel.Properties.SHOULD_DO;
import static com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel.Properties.TEXT;

/**
 * Model to provide necessary parameters for creating a reading exercise from the given text.
 *
 * @author Anton Skripin
 */
public class ReadingExerciseWithTextModel extends AbstractCommandModel<ReadingExerciseWithTextModel>
    implements IConfirmationModel {
  private String text;
  private final CommandConfirmationModel confirmationModel = new CommandConfirmationModel();

  /**
   * Getter for {@link #text}.
   *
   * @return {@link #text}
   */
  public String getText() {
    return text;
  }

  /**
   * Setter for {@link #text}.
   *
   * @param text {@link #text}
   */
  public void setText(String text) {
    this.text = text;
  }

  @Override
  public AbstractCommandModel<ReadingExerciseWithTextModel> defineMapping() {
    return this.addMapping(TEXT, ReadingExerciseWithTextModel::setText)
        .addMapping(SHOULD_DO, ReadingExerciseWithTextModel::parseValue);
  }

  @Override
  public void parseValue(String value) {
    confirmationModel.parseValue(value);
  }

  @Override
  public boolean shouldDo() {
    return confirmationModel.shouldDo();
  }
}
