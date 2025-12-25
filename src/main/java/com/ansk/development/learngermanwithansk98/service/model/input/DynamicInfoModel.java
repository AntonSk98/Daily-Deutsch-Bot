package com.ansk.development.learngermanwithansk98.service.model.input;

import static com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel.Properties.LANGUAGE;
import static com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel.Properties.SHOULD_DO;
import static com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel.Properties.TOPIC;

/**
 * Input model for information posts.
 *
 * @author Anton Skripin
 */
public class DynamicInfoModel extends AbstractCommandModel<DynamicInfoModel>
    implements IConfirmationModel {
  private String topic;
  private String language;
  private final CommandConfirmationModel commandConfirmationModel = new CommandConfirmationModel();

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
   * Getter for {@link #language}.
   *
   * @return {@link #language}
   */
  public String getLanguage() {
    return language;
  }

  /**
   * Setter for {@link #language}.
   *
   * @param language {@link #language}
   */
  public void setLanguage(String language) {
    this.language = language;
  }

  @Override
  public AbstractCommandModel<DynamicInfoModel> defineMapping() {
    return this.addMapping(TOPIC, DynamicInfoModel::setTopic)
        .addMapping(LANGUAGE, DynamicInfoModel::setLanguage)
        .addMapping(SHOULD_DO, DynamicInfoModel::parseValue);
  }

  @Override
  public void parseValue(String value) {
    commandConfirmationModel.parseValue(value);
  }

  @Override
  public boolean shouldDo() {
    return commandConfirmationModel.shouldDo();
  }
}
