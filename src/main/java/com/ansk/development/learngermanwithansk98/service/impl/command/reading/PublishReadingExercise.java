package com.ansk.development.learngermanwithansk98.service.impl.command.reading;

import static com.ansk.development.learngermanwithansk98.service.model.Command.READING_EXERCISE_PUBLISH;

import com.ansk.development.learngermanwithansk98.config.BotConfigurationProperties;
import com.ansk.development.learngermanwithansk98.config.CommandsConfigurationProperties;
import com.ansk.development.learngermanwithansk98.integration.telegram.ITelegramClient;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.repository.ReadingExerciseCache;
import com.ansk.development.learngermanwithansk98.service.impl.command.AbstractPublishExerciseSupport;
import com.ansk.development.learngermanwithansk98.service.model.Command;
import com.ansk.development.learngermanwithansk98.service.model.output.ReadingExercise;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.springframework.stereotype.Service;

/**
 * Service to publish a {@link ReadingExercise}.
 *
 * @author Anton Skripin
 */
@Service
public class PublishReadingExercise extends AbstractPublishExerciseSupport {

  private final ReadingExerciseCache readingExerciseCache;
  private final ITelegramClient outputGateway;

  /**
   * Constructor.
   *
   * @param commandsConfiguration See {@link CommandsConfigurationProperties}
   * @param telegramClient See {@link ITelegramClient}
   * @param commandCache See {@link CommandCache}
   * @param botConfiguration See {@link BotConfigurationProperties}
   */
  protected PublishReadingExercise(
      CommandsConfigurationProperties commandsConfiguration,
      ITelegramClient telegramClient,
      CommandCache commandCache,
      BotConfigurationProperties botConfiguration,
      ReadingExerciseCache readingExerciseCache) {
    super(commandsConfiguration, telegramClient, commandCache, botConfiguration);
    this.readingExerciseCache = readingExerciseCache;
    this.outputGateway = telegramClient;
  }

  @Override
  public Supplier<Boolean> isPresentInCache() {
    return () -> readingExerciseCache.cachedReadingExercise().isPresent();
  }

  @Override
  public Consumer<Long> publish() {
    return groupId ->
        outputGateway.sendReadingExercise(
            groupId, readingExerciseCache.cachedReadingExercise().orElseThrow());
  }

  @Override
  public Runnable clearCache() {
    return readingExerciseCache::clearCache;
  }

  @Override
  public Command supportedCommand() {
    return READING_EXERCISE_PUBLISH;
  }
}
