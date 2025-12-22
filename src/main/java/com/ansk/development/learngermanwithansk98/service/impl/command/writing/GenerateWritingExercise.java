package com.ansk.development.learngermanwithansk98.service.impl.command.writing;

import static com.ansk.development.learngermanwithansk98.service.model.Command.WRITING_WITH_EXAMPLE;

import com.ansk.development.learngermanwithansk98.config.CommandsConfigurationProperties;
import com.ansk.development.learngermanwithansk98.config.WritingPrompts;
import com.ansk.development.learngermanwithansk98.integration.openai.OpenAiClient;
import com.ansk.development.learngermanwithansk98.integration.telegram.ITelegramClient;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.repository.WritingExerciseCache;
import com.ansk.development.learngermanwithansk98.service.impl.pipe.WritingExerciseDocumentPipe;
import com.ansk.development.learngermanwithansk98.service.model.Command;
import org.springframework.stereotype.Service;

/**
 * Service to generate a writing exercise.
 *
 * @author Anton Skripin
 */
@Service
public class GenerateWritingExercise extends WritingExerciseSupport {

  /**
   * Constructor.
   *
   * @param commandsConfiguration See {@link CommandsConfigurationProperties}
   * @param telegramClient See {@link ITelegramClient}
   * @param commandCache See {@link CommandCache}
   * @param OpenAiClient See {@link OpenAiClient}
   * @param promptsConfiguration See {@link WritingPrompts}
   * @param writingExerciseDocumentPipe See {@link WritingExerciseDocumentPipe}
   * @param writingExerciseCache See {@link WritingExerciseCache}
   */
  protected GenerateWritingExercise(
      CommandsConfigurationProperties commandsConfiguration,
      ITelegramClient telegramClient,
      CommandCache commandCache,
      OpenAiClient OpenAiClient,
      WritingPrompts promptsConfiguration,
      WritingExerciseDocumentPipe writingExerciseDocumentPipe,
      WritingExerciseCache writingExerciseCache) {
    super(
        commandsConfiguration,
        telegramClient,
        commandCache,
        OpenAiClient,
        promptsConfiguration,
        writingExerciseDocumentPipe,
        writingExerciseCache);
  }

  @Override
  public Command supportedCommand() {
    return WRITING_WITH_EXAMPLE;
  }
}
