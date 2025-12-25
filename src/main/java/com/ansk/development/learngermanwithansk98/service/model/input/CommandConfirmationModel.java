package com.ansk.development.learngermanwithansk98.service.model.input;

import static com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel.Properties.APPROVE_PROMPT;

import org.apache.commons.lang3.StringUtils;

/**
 * General model that contains confirmation parameter to perform some action.
 *
 * @author Anton Skripin
 */
public class CommandConfirmationModel extends AbstractCommandModel<CommandConfirmationModel>
    implements IConfirmationModel {
  private boolean shouldDo;

  @Override
  public void parseValue(String value) {
    this.shouldDo = StringUtils.isNotEmpty(value) && value.contains(APPROVE_PROMPT);
  }

  @Override
  public boolean shouldDo() {
    return shouldDo;
  }

  @Override
  public AbstractCommandModel<CommandConfirmationModel> defineMapping() {
    return this;
  }
}
