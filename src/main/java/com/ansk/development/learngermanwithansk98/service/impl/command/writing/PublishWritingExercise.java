package com.ansk.development.learngermanwithansk98.service.impl.command.writing;

import static com.ansk.development.learngermanwithansk98.service.model.Command.PUBLISH_WRITING_EXERCISE;

import com.ansk.development.learngermanwithansk98.config.BotConfigurationProperties;
import com.ansk.development.learngermanwithansk98.config.CommandsConfigurationProperties;
import com.ansk.development.learngermanwithansk98.integration.telegram.ITelegramClient;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.repository.WritingExerciseCache;
import com.ansk.development.learngermanwithansk98.service.impl.command.AbstractPublishExerciseSupport;
import com.ansk.development.learngermanwithansk98.service.model.Command;
import com.ansk.development.learngermanwithansk98.service.model.output.WritingExercise;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.springframework.stereotype.Service;

/**
 * Service to publish a cached {@link WritingExercise}.
 *
 * @author Anton Skripin
 */
@Service
public class PublishWritingExercise extends AbstractPublishExerciseSupport {

  private final WritingExerciseCache writingExerciseCache;
  private final ITelegramClient outputGateway;

  /**
   * Constructor.
   *
   * @param commandsConfiguration See {@link CommandsConfigurationProperties}
   * @param telegramClient See {@link ITelegramClient}
   * @param commandCache See {@link CommandCache}
   * @param botConfiguration See {@link WritingExerciseCache}
   */
  protected PublishWritingExercise(
      CommandsConfigurationProperties commandsConfiguration,
      ITelegramClient telegramClient,
      CommandCache commandCache,
      BotConfigurationProperties botConfiguration,
      WritingExerciseCache writingExerciseCache) {
    super(commandsConfiguration, telegramClient, commandCache, botConfiguration);
    this.writingExerciseCache = writingExerciseCache;
    this.outputGateway = telegramClient;
  }

  @Override
  public Supplier<Boolean> isPresentInCache() {
    return () -> writingExerciseCache.cachedWritingExercise().isPresent();
  }

  @Override
  public Consumer<Long> publish() {
    return groupId -> {
      WritingExercise writingExercise = writingExerciseCache.cachedWritingExercise().orElseThrow();
      outputGateway.sendWritingExercise(groupId, writingExercise);
    };
  }

  @Override
  public Runnable clearCache() {
    return writingExerciseCache::clearCache;
  }

  @Override
  public Command supportedCommand() {
    return PUBLISH_WRITING_EXERCISE;
  }
}
