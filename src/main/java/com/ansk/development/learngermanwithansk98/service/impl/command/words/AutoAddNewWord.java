package com.ansk.development.learngermanwithansk98.service.impl.command.words;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.config.WordCardPromptsConfiguration;
import com.ansk.development.learngermanwithansk98.integration.openai.OpenAiClient;
import com.ansk.development.learngermanwithansk98.integration.telegram.ITelegramClient;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.repository.WordCache;
import com.ansk.development.learngermanwithansk98.service.impl.command.AbstractCommandProcessor;
import com.ansk.development.learngermanwithansk98.service.model.Command;
import com.ansk.development.learngermanwithansk98.service.model.GenericPromptTemplate;
import com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel;
import com.ansk.development.learngermanwithansk98.service.model.input.AutoWordCompletionModel;
import com.ansk.development.learngermanwithansk98.service.model.input.CommandParameters;
import com.ansk.development.learngermanwithansk98.service.model.input.Word;
import org.springframework.stereotype.Service;

import static com.ansk.development.learngermanwithansk98.service.model.Command.AI_NEW_WORD;
import static com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel.Properties.WORD;

/**
 * Service that adds a new word automatically without the client interaction.
 *
 * @author Anton Skripin
 */
@Service
public class AutoAddNewWord extends AbstractCommandProcessor {

    private final ITelegramClient telegramClient;
    private final OpenAiClient OpenAiClient;
    private final WordCardPromptsConfiguration promptsConfiguration;
    private final WordCache wordCache;

    /**
     * Constructor.
     *
     * @param commandsConfiguration See {@link CommandsConfiguration}
     * @param telegramClient See {@link ITelegramClient}
     * @param commandCache          See {@link CommandCache}
     * @param OpenAiClient             See {@link OpenAiClient}
     * @param promptsConfiguration  See {@link WordCardPromptsConfiguration}
     * @param wordCache             See {@link WordCache}
     */
    protected AutoAddNewWord(CommandsConfiguration commandsConfiguration,
                             ITelegramClient telegramClient,
                             CommandCache commandCache,
                             OpenAiClient OpenAiClient,
                             WordCardPromptsConfiguration promptsConfiguration,
                             WordCache wordCache) {
        super(commandsConfiguration, telegramClient, commandCache);
        this.telegramClient = telegramClient;
        this.OpenAiClient = OpenAiClient;
        this.promptsConfiguration = promptsConfiguration;
        this.wordCache = wordCache;
    }

    @Override
    public Command supportedCommand() {
        return AI_NEW_WORD;
    }

    @Override
    public void applyCommandModel(AbstractCommandModel<?> model, CommandParameters parameters) {
        AutoWordCompletionModel autoWordCompletionModel = model.map(AutoWordCompletionModel.class);

        telegramClient.sendPlainMessage(parameters.chatId(), "Filling out the word info...");
        GenericPromptTemplate autoDefineWord = new GenericPromptTemplate(promptsConfiguration.autoWordDefinition())
                .resolveVariable(WORD, autoWordCompletionModel.getWord());
        var word = OpenAiClient.sendRequest(autoDefineWord.getPrompt(), Word.class);
        wordCache.addWord(word);

        telegramClient.sendMessageWithPayload(parameters.chatId(), "Word is added to cache!", word);
    }

    @Override
    public AbstractCommandModel<?> supportedModelWithMapping() {
        return new AutoWordCompletionModel()
                .init()
                .addMapping(WORD, AutoWordCompletionModel::setWord);
    }
}
