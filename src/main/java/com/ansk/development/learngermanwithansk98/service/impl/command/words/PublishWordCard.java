package com.ansk.development.learngermanwithansk98.service.impl.command.words;

import static com.ansk.development.learngermanwithansk98.service.impl.MapperUtils.mapToDateGermanFormat;

import com.ansk.development.learngermanwithansk98.config.BotConfigurationProperties;
import com.ansk.development.learngermanwithansk98.config.CommandsConfigurationProperties;
import com.ansk.development.learngermanwithansk98.integration.telegram.ITelegramClient;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.repository.WordCache;
import com.ansk.development.learngermanwithansk98.service.impl.command.AbstractPublishExerciseSupport;
import com.ansk.development.learngermanwithansk98.service.impl.pipe.CardToImagesConverterPipe;
import com.ansk.development.learngermanwithansk98.service.model.Command;
import com.ansk.development.learngermanwithansk98.service.model.output.ExerciseDocument;
import com.ansk.development.learngermanwithansk98.service.model.output.WordCard;
import java.time.LocalDate;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * Service that publishes a word card to a group.
 *
 * @author Anton Skripin
 */
@Service
public class PublishWordCard extends AbstractPublishExerciseSupport {

  private final ITelegramClient telegramClient;
  private final WordCache wordCache;
  private final CardToImagesConverterPipe converterPipe;

  /**
   * Constructor.
   *
   * @param commandsConfiguration See {@link CommandsConfigurationProperties}
   * @param telegramClient See {@link ITelegramClient}
   * @param commandCache See {@link CommandCache}
   */
  protected PublishWordCard(
      CommandsConfigurationProperties commandsConfiguration,
      ITelegramClient telegramClient,
      CommandCache commandCache,
      BotConfigurationProperties botConfiguration,
      WordCache wordCache,
      CardToImagesConverterPipe converterPipe) {
    super(commandsConfiguration, telegramClient, commandCache, botConfiguration);
    this.telegramClient = telegramClient;
    this.wordCache = wordCache;
    this.converterPipe = converterPipe;
  }

  @Override
  public Command supportedCommand() {
    return Command.PUBLISH_WORD_CARD;
  }

  @Override
  public Supplier<Boolean> isPresentInCache() {
    return () -> !CollectionUtils.isEmpty(wordCache.getWords());
  }

  @Override
  public Consumer<Long> publish() {
    return groupId -> {
      WordCard previewWordCard =
          new WordCard(mapToDateGermanFormat(LocalDate.now()), wordCache.getWords());
      ExerciseDocument wordCardDocumentToPublish = converterPipe.pipe(previewWordCard);
      telegramClient.sendWordCard(groupId, wordCardDocumentToPublish);
    };
  }

  @Override
  public Runnable clearCache() {
    return wordCache::cleanCache;
  }
}
