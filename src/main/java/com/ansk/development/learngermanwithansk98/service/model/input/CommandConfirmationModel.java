package com.ansk.development.learngermanwithansk98.service.model.input;

import org.apache.commons.lang3.StringUtils;

import static com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel.Properties.APPROVE_PROMPT;

/**
 * General model that contains confirmation parameter to perform some action.
 *
 * @author Anton Skripin
 */
public class CommandConfirmationModel {
    private boolean shouldDo;

    public boolean shouldDo() {
        return shouldDo;
    }

    public void setShouldDo(String value) {
        this.shouldDo = StringUtils.isNotEmpty(value) && value.contains(APPROVE_PROMPT);
    }
}
