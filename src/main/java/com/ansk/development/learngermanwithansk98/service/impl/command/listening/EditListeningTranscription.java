package com.ansk.development.learngermanwithansk98.service.impl.command.listening;

import com.ansk.development.learngermanwithansk98.config.CommandsConfigurationProperties;
import com.ansk.development.learngermanwithansk98.integration.telegram.ITelegramClient;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.repository.ListeningExerciseCache;
import com.ansk.development.learngermanwithansk98.service.impl.command.AbstractCommandProcessor;
import com.ansk.development.learngermanwithansk98.service.impl.pipe.ListeningExerciseDocumentPipe;
import com.ansk.development.learngermanwithansk98.service.model.Command;
import com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel;
import com.ansk.development.learngermanwithansk98.service.model.input.CommandParameters;
import com.ansk.development.learngermanwithansk98.service.model.input.EditListeningExerciseModel;
import com.ansk.development.learngermanwithansk98.service.model.output.EditListeningExerciseContext;
import com.ansk.development.learngermanwithansk98.service.model.output.ListeningExercise;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Service that edits existing {@link ListeningExercise}.
 *
 * @author Anton Skripin
 */
@Service
public class EditListeningTranscription extends AbstractCommandProcessor {

  private static final String SEPARATOR = "\n\n";

  private final ListeningExerciseCache listeningExerciseCache;
  private final ITelegramClient outputGateway;
  private final ListeningExerciseDocumentPipe documentPipe;

  /**
   * Constructor.
   *
   * @param commandsConfiguration See {@link CommandsConfigurationProperties}
   * @param telegramClient See {@link ITelegramClient}
   * @param commandCache See {@link CommandCache}
   * @param listeningExerciseCache See {@link ListeningExerciseCache}
   * @param documentPipe See {@link ListeningExerciseDocumentPipe}
   */
  protected EditListeningTranscription(
      CommandsConfigurationProperties commandsConfiguration,
      ITelegramClient telegramClient,
      CommandCache commandCache,
      ListeningExerciseCache listeningExerciseCache,
      ListeningExerciseDocumentPipe documentPipe) {
    super(commandsConfiguration, telegramClient, commandCache);
    this.listeningExerciseCache = listeningExerciseCache;
    this.outputGateway = telegramClient;
    this.documentPipe = documentPipe;
  }

  @Override
  public Command supportedCommand() {
    return Command.LISTENING_EDIT_TRANSCRIPTION;
  }

  @Override
  public void applyCommandModel(AbstractCommandModel<?> model, CommandParameters parameters) {
    EditListeningExerciseModel editListeningExercise = model.map(EditListeningExerciseModel.class);
    List<String> newTranscriptionSplitByParagraphs =
        Arrays.stream(editListeningExercise.getEditedTranscription().split(SEPARATOR)).toList();

    ListeningExercise storedExercise =
        listeningExerciseCache
            .cachedListeningExercise()
            .orElseThrow(() -> new IllegalStateException("No listening exercise in cache!"));

    ListeningExercise listeningExercise =
        new ListeningExercise(
            storedExercise.audio(),
            storedExercise.level(),
            storedExercise.title(),
            storedExercise.tasks(),
            documentPipe.pipe(
                new ListeningExercise.Document(
                    storedExercise.level(),
                    storedExercise.title(),
                    newTranscriptionSplitByParagraphs,
                    storedExercise.tasks().tasks().stream()
                        .map(ListeningExercise.Task::question)
                        .toList())),
            newTranscriptionSplitByParagraphs);

    listeningExerciseCache.saveListeningExercise(listeningExercise);

    outputGateway.sendPlainMessage(
        parameters.chatId(), "Edited listening exercise stored in cache!");
  }

  @Override
  public AbstractCommandModel<?> supportedModelWithMapping() {
    return new EditListeningExerciseModel().defineMapping();
  }

  @Override
  public void providePromptContext(
      AbstractCommandModel<?> currentModelState, CommandParameters parameters) {
    if (listeningExerciseCache.cachedListeningExercise().isEmpty()) {
      throw new IllegalStateException("No listening exercise stored in cache...");
    }

    String audio = listeningExerciseCache.cachedListeningExercise().get().audio();

    String transcription =
        String.join(
            SEPARATOR, listeningExerciseCache.cachedListeningExercise().get().transcription());

    outputGateway.sendPromptToEditListeningExercise(
        parameters.chatId(), new EditListeningExerciseContext(audio, transcription));
  }
}
