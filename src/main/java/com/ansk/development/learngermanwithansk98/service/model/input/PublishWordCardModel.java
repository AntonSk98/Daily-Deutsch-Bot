package com.ansk.development.learngermanwithansk98.service.model.input;

/**
 * Input model for publishing a new word card.
 *
 * @author Anton Skripin
 */
public class PublishWordCardModel extends AbstractCommandModel<PublishWordCardModel> implements IConfirmationModel {
    private final CommandConfirmationModel confirmationModel = new CommandConfirmationModel();

    @Override
    public AbstractCommandModel<PublishWordCardModel> init() {
        return new PublishWordCardModel();
    }

    @Override
    public void parseValue(String value) {
        confirmationModel.setShouldDo(value);
    }

    @Override
    public boolean shouldDo() {
        return confirmationModel.shouldDo();
    }
}
