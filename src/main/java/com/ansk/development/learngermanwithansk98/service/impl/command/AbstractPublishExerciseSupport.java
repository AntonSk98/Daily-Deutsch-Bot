package com.ansk.development.learngermanwithansk98.service.impl.command;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.config.DailyDeutschBotConfiguration;
import com.ansk.development.learngermanwithansk98.gateway.telegram.ITelegramOutputGateway;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel;
import com.ansk.development.learngermanwithansk98.service.model.input.CommandConfirmationModel;
import com.ansk.development.learngermanwithansk98.service.model.input.CommandParameters;
import com.ansk.development.learngermanwithansk98.service.model.input.IConfirmationModel;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel.Properties.SHOULD_DO;

/**
 * Abstract class that facilitates the process of publishing exercise into a group.
 *
 * @author Anton Skripin
 */
public abstract class AbstractPublishExerciseSupport extends AbstractCommandProcessor {


    private final Long groupId;
    private final ITelegramOutputGateway outputGateway;

    /**
     * Constructor.
     *
     * @param commandsConfiguration See {@link CommandsConfiguration}
     * @param telegramOutputGateway See {@link ITelegramOutputGateway}
     * @param commandCache          See {@link CommandCache}
     * @param botConfiguration      See {@link DailyDeutschBotConfiguration}
     */
    protected AbstractPublishExerciseSupport(CommandsConfiguration commandsConfiguration,
                                             ITelegramOutputGateway telegramOutputGateway,
                                             CommandCache commandCache,
                                             DailyDeutschBotConfiguration botConfiguration) {
        super(commandsConfiguration, telegramOutputGateway, commandCache);
        this.groupId = botConfiguration.groupId();
        this.outputGateway = telegramOutputGateway;
    }

    /**
     * Supplier to checks whether an exercise that is about to be published is present in cache.
     *
     * @return supplier that checks if exercise is present in cache
     */
    public abstract Supplier<Boolean> isPresentInCache();

    /**
     * Consumer that provides a logic to publish an exercise into a group
     *
     * @return consumer that publishes an exercise
     */
    public abstract Consumer<Long> publish();

    /**
     * Runnable that cleans up a cache after publishing an exercise.
     *
     * @return runnable to clean up the cache
     */
    public abstract Runnable clearCache();

    @Override
    public void applyCommandModel(AbstractCommandModel<?> model, CommandParameters parameters) {
        IConfirmationModel confirmationModel = model.map(IConfirmationModel.class);

        if (!confirmationModel.shouldDo()) {
            outputGateway.sendPlainMessage(parameters.chatId(), "Publishing rejected.");
            return;
        }

        if (!isPresentInCache().get()) {
            outputGateway.sendPlainMessage(parameters.chatId(), "Cannot publish as the cache is empty.");
            return;
        }

        outputGateway.sendPlainMessage(parameters.chatId(), "Publishing...");
        publish().accept(groupId);
        outputGateway.sendPlainMessage(parameters.chatId(), "Published!");
        clearCache().run();
        outputGateway.sendPlainMessage(parameters.chatId(), "Cache is cleaned");
    }

    @Override
    public AbstractCommandModel<?> supportedModelWithMapping() {
        return new CommandConfirmationModel()
                .init()
                .addMapping(SHOULD_DO, CommandConfirmationModel::parseValue);
    }
}
