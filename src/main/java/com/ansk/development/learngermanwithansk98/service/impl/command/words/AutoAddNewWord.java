package com.ansk.development.learngermanwithansk98.service.impl.command.words;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.config.WordCardPromptsConfiguration;
import com.ansk.development.learngermanwithansk98.gateway.telegram.ITelegramOutputGateway;
import com.ansk.development.learngermanwithansk98.gateway.openai.OpenAiGateway;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.repository.WordCache;
import com.ansk.development.learngermanwithansk98.service.impl.command.AbstractCommandService;
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
 * Service that allows to define a word automatically with the help of AI.
 *
 * @author Anton Skripin
 */
@Service
public class AutoAddNewWord extends AbstractCommandService {

    private final ITelegramOutputGateway telegramOutputGateway;
    private final OpenAiGateway openAiGateway;
    private final WordCardPromptsConfiguration promptsConfiguration;
    private final WordCache wordCache;

    protected AutoAddNewWord(CommandsConfiguration commandsConfiguration,
                             ITelegramOutputGateway telegramOutputGateway,
                             CommandCache commandCache,
                             OpenAiGateway openAiGateway,
                             WordCardPromptsConfiguration promptsConfiguration,
                             WordCache wordCache) {
        super(commandsConfiguration, telegramOutputGateway, commandCache);
        this.telegramOutputGateway = telegramOutputGateway;
        this.openAiGateway = openAiGateway;
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

        telegramOutputGateway.sendPlainMessage(parameters.chatId(), "Filling out the word info...");
        GenericPromptTemplate autoDefineWord = new GenericPromptTemplate(promptsConfiguration.autoWordDefinition())
                .resolveVariable(WORD, autoWordCompletionModel.getWord());
        var word = openAiGateway.sendRequest(autoDefineWord.getPrompt(), Word.class);
        wordCache.addWord(word);

        telegramOutputGateway.sendMessageWithPayload(parameters.chatId(), "Word is added to cache!", word);
    }

    @Override
    public AbstractCommandModel<?> supportedCommandModel() {
        return new AutoWordCompletionModel()
                .init()
                .addMapping("word", AutoWordCompletionModel::setWord);
    }
}
