package com.ansk.development.learngermanwithansk98.service.impl.command.info;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.config.DailyDeutschBotConfiguration;
import com.ansk.development.learngermanwithansk98.integration.openai.OpenAiClient;
import com.ansk.development.learngermanwithansk98.integration.telegram.ITelegramClient;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.repository.InformationPostCache;
import com.ansk.development.learngermanwithansk98.service.impl.command.AbstractPublishExerciseSupport;
import com.ansk.development.learngermanwithansk98.service.model.Command;
import com.ansk.development.learngermanwithansk98.service.model.GenericPromptTemplate;
import com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel;
import com.ansk.development.learngermanwithansk98.service.model.input.CommandParameters;
import com.ansk.development.learngermanwithansk98.service.model.input.DynamicInfoModel;
import com.ansk.development.learngermanwithansk98.service.model.output.InformationPostModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel.Properties.*;

/**
 * Service that is responsible for creating a post with some dynamic information.
 *
 * @author Anton Skripin
 */
@Service
public class InformationPost extends AbstractPublishExerciseSupport {

    private final ITelegramClient telegramClient;
    private final OpenAiClient openAiClient;
    private final InformationPostCache informationPostCache;

    @Value("${ai.information.prompt}")
    private String requestInformationPrompt;

    /**
     * Constructor.
     *
     * @param commandsConfiguration See {@link CommandsConfiguration}
     * @param telegramClient        See {@link ITelegramClient}
     * @param commandCache          See {@link CommandCache}
     * @param botConfiguration      See {@link DailyDeutschBotConfiguration}
     */
    protected InformationPost(CommandsConfiguration commandsConfiguration,
                              ITelegramClient telegramClient,
                              CommandCache commandCache,
                              DailyDeutschBotConfiguration botConfiguration,
                              OpenAiClient openAiClient,
                              InformationPostCache informationPostCache) {
        super(commandsConfiguration, telegramClient, commandCache, botConfiguration);
        this.telegramClient = telegramClient;
        this.openAiClient = openAiClient;
        this.informationPostCache = informationPostCache;
    }

    @Override
    public Supplier<Boolean> isPresentInCache() {
        return () -> informationPostCache.getInformationPost() != null;
    }

    @Override
    public Consumer<Long> publish() {
        return groupId -> telegramClient.sendInformationPost(groupId, informationPostCache.getInformationPost());
    }

    @Override
    public Runnable clearCache() {
        return informationPostCache::clear;
    }

    @Override
    public Command supportedCommand() {
        return Command.DYNAMIC_INFO;
    }

    @Override
    public void provideDynamicPrompt(AbstractCommandModel<?> currentModelState, CommandParameters parameters) {
        DynamicInfoModel dynamicInfoModel = currentModelState.map(DynamicInfoModel.class);
        GenericPromptTemplate genericPromptTemplate = new GenericPromptTemplate(requestInformationPrompt);

        InformationPostModel informationPostModel = openAiClient.sendRequest(genericPromptTemplate
                .resolveVariable(TOPIC, dynamicInfoModel.getTopic())
                .resolveVariable(LANGUAGE, dynamicInfoModel.getLanguage())
                .getPrompt(), InformationPostModel.class);

        telegramClient.sendInformationPost(parameters.chatId(), informationPostModel);
        informationPostCache.saveInformationPost(informationPostModel);
    }

    @Override
    public AbstractCommandModel<?> supportedModelWithMapping() {
        return new DynamicInfoModel()
                .init()
                .addMapping(TOPIC, DynamicInfoModel::setTopic)
                .addMapping(LANGUAGE, DynamicInfoModel::setLanguage)
                .addMapping(SHOULD_DO, DynamicInfoModel::parseValue);
    }
}
