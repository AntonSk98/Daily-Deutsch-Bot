package com.ansk.development.learngermanwithansk98.service.model.input;

import static com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel.Properties.APPROVE_PROMPT;
import static com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel.Properties.CORRECTED_TEXT;
import static com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel.Properties.SHOULD_DO;
import static com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel.Properties.TOPIC;
import static com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel.Properties.WITH_AUDIO;

import org.apache.commons.lang3.StringUtils;

/**
 * Model that contains input parameters in order to create a text with its corrected version.
 *
 * @author Anton Skripin
 */
public class CorrectTextModel extends AbstractCommandModel<CorrectTextModel>
    implements IConfirmationModel {
  private String topic;
  private String textWithCorrections;
  private boolean withAudio;
  private final CommandConfirmationModel confirmationModel = new CommandConfirmationModel();

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
   * Getter for {@link #withAudio}.
   *
   * @return {@link #withAudio}
   */
  public boolean isWithAudio() {
    return withAudio;
  }

  /**
   * Setter for {@link #withAudio}.
   *
   * @param value {@link #withAudio}
   */
  public void withAudio(String value) {
    this.withAudio = StringUtils.isNotEmpty(value) && value.contains(APPROVE_PROMPT);
  }

  @Override
  public AbstractCommandModel<CorrectTextModel> defineMapping() {
    return this.addMapping(TOPIC, CorrectTextModel::setTopic)
        .addMapping(CORRECTED_TEXT, CorrectTextModel::setTextWithCorrections)
        .addMapping(SHOULD_DO, CorrectTextModel::parseValue)
        .addMapping(WITH_AUDIO, CorrectTextModel::withAudio);
  }

  @Override
  public void parseValue(String value) {
    this.confirmationModel.parseValue(value);
  }

  @Override
  public boolean shouldDo() {
    return confirmationModel.shouldDo();
  }
}
