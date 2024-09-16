package com.ansk.development.learngermanwithansk98.service.impl.command.listening;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.gateway.telegram.ITelegramOutputGateway;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.repository.ListeningExerciseCache;
import com.ansk.development.learngermanwithansk98.service.impl.command.AbstractCommandProcessor;
import com.ansk.development.learngermanwithansk98.service.impl.pipe.ListeningExerciseDocumentPipe;
import com.ansk.development.learngermanwithansk98.service.model.Command;
import com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel;
import com.ansk.development.learngermanwithansk98.service.model.input.CommandParameters;
import com.ansk.development.learngermanwithansk98.service.model.input.EditListeningExerciseModel;
import com.ansk.development.learngermanwithansk98.service.model.output.EditListeningExercisePrompt;
import com.ansk.development.learngermanwithansk98.service.model.output.ListeningExercise;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Service that edits existing {@link ListeningExercise}.
 *
 * @author Anton Skripin
 */
@Service
public class EditListeningTranscription extends AbstractCommandProcessor {

    private static final String SEPARATOR = "\n\n";

    private final ListeningExerciseCache listeningExerciseCache;
    private final ITelegramOutputGateway outputGateway;
    private final ListeningExerciseDocumentPipe documentPipe;

    /**
     * Constructor.
     *
     * @param commandsConfiguration  See {@link CommandsConfiguration}
     * @param telegramOutputGateway  See {@link ITelegramOutputGateway}
     * @param commandCache           See {@link CommandCache}
     * @param listeningExerciseCache See {@link ListeningExerciseCache}
     * @param documentPipe           See {@link ListeningExerciseDocumentPipe}
     */
    protected EditListeningTranscription(CommandsConfiguration commandsConfiguration,
                                         ITelegramOutputGateway telegramOutputGateway,
                                         CommandCache commandCache,
                                         ListeningExerciseCache listeningExerciseCache,
                                         ListeningExerciseDocumentPipe documentPipe) {
        super(commandsConfiguration, telegramOutputGateway, commandCache);
        this.listeningExerciseCache = listeningExerciseCache;
        this.outputGateway = telegramOutputGateway;
        this.documentPipe = documentPipe;
    }

    @Override
    public Command supportedCommand() {
        return Command.LISTENING_TRANSCRIPTION;
    }

    @Override
    public void applyCommandModel(AbstractCommandModel<?> model, CommandParameters parameters) {
        EditListeningExerciseModel editListeningExercise = model.map(EditListeningExerciseModel.class);
        List<String> newTranscriptionSplitByParagraphs = Arrays.stream(editListeningExercise.getEditedTranscription().split(SEPARATOR)).toList();

        ListeningExercise storedExercise = listeningExerciseCache.cachedListeningExercise().orElseThrow(() -> new IllegalStateException("No listening exercise in cache!"));

        ListeningExercise listeningExercise = new ListeningExercise(
                storedExercise.audio(),
                storedExercise.level(),
                storedExercise.title(),
                storedExercise.tasks(),
                documentPipe.pipe(new ListeningExercise.Document(
                                storedExercise.level(),
                                storedExercise.title(),
                                newTranscriptionSplitByParagraphs,
                                storedExercise.tasks().tasks().stream().map(ListeningExercise.Task::question).toList()
                        )
                ),
                newTranscriptionSplitByParagraphs
        );

        listeningExerciseCache.saveListeningExercise(listeningExercise);

        outputGateway.sendPlainMessage(parameters.chatId(), "Edited listening exercise stored in cache!");
    }

    @Override
    public AbstractCommandModel<?> supportedModelWithMapping() {
        return new EditListeningExerciseModel().addMapping("transcription", EditListeningExerciseModel::setEditedTranscription);
    }

    @Override
    public void provideDynamicPrompt(CommandParameters parameters) {
        if (listeningExerciseCache.cachedListeningExercise().isEmpty()) {
            throw new IllegalStateException("No listening exercise stored in cache...");
        }

        String audio = listeningExerciseCache.cachedListeningExercise().get().audio();

        String transcription = String.join(SEPARATOR, listeningExerciseCache.cachedListeningExercise().get().transcription());

        outputGateway.sendPromptToEditListeningExercise(parameters.chatId(), new EditListeningExercisePrompt(audio, transcription));
    }
}
