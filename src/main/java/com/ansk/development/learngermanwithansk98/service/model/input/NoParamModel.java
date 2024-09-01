package com.ansk.development.learngermanwithansk98.service.model;

/**
 * Common model for all commands that do not require parameters for their execution.
 *
 * @author Anton Skripin
 */
public class NoParamModel extends AbstractCommandModel<NoParamModel> {

    @Override
    public AbstractCommandModel<NoParamModel> init() {
        return new NoParamModel();
    }
}
