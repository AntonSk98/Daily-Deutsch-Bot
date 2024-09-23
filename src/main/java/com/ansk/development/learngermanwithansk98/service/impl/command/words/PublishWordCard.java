package com.ansk.development.learngermanwithansk98.service.impl.command.words;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.config.DailyDeutschBotConfiguration;
import com.ansk.development.learngermanwithansk98.gateway.telegram.ITelegramOutputGateway;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.repository.WordCache;
import com.ansk.development.learngermanwithansk98.service.impl.command.AbstractCommandProcessor;
import com.ansk.development.learngermanwithansk98.service.impl.pipe.CardToImagesConverterPipe;
import com.ansk.development.learngermanwithansk98.service.model.Command;
import com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel;
import com.ansk.development.learngermanwithansk98.service.model.input.CommandParameters;
import com.ansk.development.learngermanwithansk98.service.model.input.PublishWordCardModel;
import com.ansk.development.learngermanwithansk98.service.model.output.ExerciseDocument;
import com.ansk.development.learngermanwithansk98.service.model.output.WordCard;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;

import static com.ansk.development.learngermanwithansk98.service.impl.MapperUtils.mapToDateGermanFormat;
import static com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel.Properties.SHOULD_DO;

/**
 * Service that publishes a word card to a group.
 *
 * @author Anton Skripin
 */
@Service
public class PublishWordCard extends AbstractCommandProcessor {

    private final ITelegramOutputGateway telegramOutputGateway;
    private final DailyDeutschBotConfiguration botConfiguration;
    private final WordCache wordCache;
    private final CardToImagesConverterPipe converterPipe;

    /**
     * Constructor.
     *
     * @param commandsConfiguration See {@link CommandsConfiguration}
     * @param telegramOutputGateway See {@link ITelegramOutputGateway}
     * @param commandCache          See {@link CommandCache}
     */
    protected PublishWordCard(CommandsConfiguration commandsConfiguration,
                              ITelegramOutputGateway telegramOutputGateway,
                              CommandCache commandCache,
                              DailyDeutschBotConfiguration botConfiguration,
                              WordCache wordCache,
                              CardToImagesConverterPipe converterPipe) {
        super(commandsConfiguration, telegramOutputGateway, commandCache);
        this.telegramOutputGateway = telegramOutputGateway;
        this.botConfiguration = botConfiguration;
        this.wordCache = wordCache;
        this.converterPipe = converterPipe;
    }

    @Override
    public Command supportedCommand() {
        return Command.PUBLISH_WORD_CARD;
    }

    @Override
    public void applyCommandModel(AbstractCommandModel<?> model, CommandParameters parameters) {
        final Long groupId = botConfiguration.groupId();
        PublishWordCardModel publishWordCardModel = model.map(PublishWordCardModel.class);

        if (!publishWordCardModel.shouldDo()) {
            telegramOutputGateway.sendPlainMessage(parameters.chatId(), "Action to publish a word card is rejected.");
            return;
        }

        if (CollectionUtils.isEmpty(wordCache.getWords())) {
            telegramOutputGateway.sendPlainMessage(parameters.chatId(), "No word card can currently be published!");
            return;
        }

        WordCard previewWordCard = new WordCard(mapToDateGermanFormat(LocalDate.now()), wordCache.getWords());
        ExerciseDocument wordCardDocumentToPublish = converterPipe.pipe(previewWordCard);

        telegramOutputGateway.sendPlainMessage(parameters.chatId(), "Publishing the word card into the group...");
        telegramOutputGateway.sendWordCard(groupId, wordCardDocumentToPublish);
        telegramOutputGateway.sendPlainMessage(parameters.chatId(), "Word card is published. Well done!");
        wordCache.cleanCache();
        telegramOutputGateway.sendPlainMessage(parameters.chatId(), "Word cache was evicted.");
    }

    @Override
    public AbstractCommandModel<?> supportedModelWithMapping() {
        return new PublishWordCardModel()
                .init()
                .addMapping(SHOULD_DO, PublishWordCardModel::parseValue);
    }
}
