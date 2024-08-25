package com.ansk.development.learngermanwithansk98.service.impl;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.gateway.OutputGateway;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.repository.WordCache;
import com.ansk.development.learngermanwithansk98.service.model.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Service that returns currently cached {@link Word}s.
 *
 * @author Anton Skripin
 */
@Service
public class GetCachedWordsCommandService extends AbstractCommandService {

    private final WordCache wordCache;
    private final OutputGateway outputGateway;

    protected GetCachedWordsCommandService(CommandsConfiguration commandsConfiguration,
                                           OutputGateway outputGateway,
                                           CommandCache commandCache,
                                           WordCache wordCache) {
        super(commandsConfiguration, outputGateway, commandCache);
        this.wordCache = wordCache;
        this.outputGateway = outputGateway;
    }

    @Override
    public Command supportedCommand() {
        return Command.GET_WORDS;
    }

    @Override
    public void applyCommandModel(AbstractCommandModel<?> currentCommandModel, CommandParameters commandParameters) {
        String cachedWordsPrettyPrinted = getCachedWordsPrettyPrinted();
        String message = StringUtils.isEmpty(cachedWordsPrettyPrinted) ? "No words in cache yet" : cachedWordsPrettyPrinted;
        outputGateway.sendPlainMessage(commandParameters.chatId(), message);
    }

    @Override
    public AbstractCommandModel<?> supportedCommandModel() {
        return new NoParamModel();
    }

    private String getCachedWordsPrettyPrinted() {
        Map<String, Long> wordToTotalOccurrences = wordCache.getWords()
                .stream()
                .map(Word::getWord)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        Map<String, Long> wordToCurrentIndex = new HashMap<>();

        return wordCache.getWords()
                .stream()
                .map(word -> {
                    String wordStr = word.getWord();
                    long currentIndex = wordToCurrentIndex.getOrDefault(wordStr, 0L) + 1;
                    wordToCurrentIndex.put(wordStr, currentIndex);

                    long totalOccurrences = wordToTotalOccurrences.getOrDefault(wordStr, 0L);
                    return totalOccurrences > 1
                            ? WordInfo.of(currentIndex, wordStr, word.getTranslation())
                            : WordInfo.of(wordStr, word.getTranslation());
                })
                .map(WordInfo::prettyPrint)
                .collect(Collectors.joining("\n"));
    }
}
