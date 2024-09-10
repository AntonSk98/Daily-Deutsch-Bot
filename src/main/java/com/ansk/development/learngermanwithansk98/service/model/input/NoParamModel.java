package com.ansk.development.learngermanwithansk98.service.model.input;

/**
 * Common model for any command that does not contain any parameters.
 *
 * @author Anton Skripin
 */
public class NoParamModel extends AbstractCommandModel<NoParamModel> {

    @Override
    public AbstractCommandModel<NoParamModel> init() {
        return new NoParamModel();
    }
}
