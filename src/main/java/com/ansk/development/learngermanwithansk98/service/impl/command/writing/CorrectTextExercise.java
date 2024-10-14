package com.ansk.development.learngermanwithansk98.service.impl.command.writing;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.config.DailyDeutschBotConfiguration;
import com.ansk.development.learngermanwithansk98.integration.openai.OpenAiClient;
import com.ansk.development.learngermanwithansk98.integration.telegram.ITelegramClient;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.repository.CorrectedTextCache;
import com.ansk.development.learngermanwithansk98.service.impl.command.AbstractPublishExerciseSupport;
import com.ansk.development.learngermanwithansk98.service.impl.command.writing.correction.CorrectionRenderer;
import com.ansk.development.learngermanwithansk98.service.impl.pipe.CorrectedTextDocumentPipe;
import com.ansk.development.learngermanwithansk98.service.model.Command;
import com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel;
import com.ansk.development.learngermanwithansk98.service.model.input.CommandParameters;
import com.ansk.development.learngermanwithansk98.service.model.input.CorrectTextModel;
import com.ansk.development.learngermanwithansk98.service.model.output.CorrectedTextDocumentMetadata;
import com.ansk.development.learngermanwithansk98.service.model.output.ExerciseDocument;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel.Properties.*;

/**
 * Service responsible for processing a text and returning both the original text and its corrected version.
 *
 * @author Anton Skripin
 */
@Service
public class CorrectTextExercise extends AbstractPublishExerciseSupport {

    private final ITelegramClient outputGateway;
    private final CorrectedTextDocumentPipe correctedTextDocumentPipe;
    private final CorrectedTextCache correctedTextCache;
    private final OpenAiClient openAiClient;

    /**
     * Constructor.
     *
     * @param commandsConfiguration     See {@link CommandsConfiguration}
     * @param telegramClient     See {@link ITelegramClient}
     * @param correctedTextDocumentPipe See {@link CorrectedTextDocumentPipe}
     * @param openAiClient              See {@link OpenAiClient}
     */
    protected CorrectTextExercise(CommandsConfiguration commandsConfiguration,
                                  ITelegramClient telegramClient,
                                  CommandCache commandCache,
                                  CorrectedTextDocumentPipe correctedTextDocumentPipe,
                                  CorrectedTextCache correctedTextCache,
                                  DailyDeutschBotConfiguration botConfiguration, OpenAiClient openAiClient) {
        super(commandsConfiguration, telegramClient, commandCache, botConfiguration);
        this.outputGateway = telegramClient;
        this.correctedTextDocumentPipe = correctedTextDocumentPipe;
        this.correctedTextCache = correctedTextCache;
        this.openAiClient = openAiClient;
    }

    @Override
    public Command supportedCommand() {
        return Command.CORRECT_TEXT;
    }

    @Override
    public Supplier<Boolean> isPresentInCache() {
        return () -> correctedTextCache.getCorrectedText().isPresent();
    }

    @Override
    public Consumer<Long> publish() {
        return groupId -> {
            CorrectedTextCache.CorrectedTexContainer correctText = correctedTextCache.getCorrectedText().orElseThrow();
            outputGateway.sendCorrectedText(
                    groupId,
                    correctText.nonCorrectedTextDocument(),
                    correctText.textWithCorrectionsDocument()
            );
        };
    }

    @Override
    public Consumer<PublishHookParameters> publishHook() {
        return publishHookParameters -> {
            CorrectTextModel correctTextModel = publishHookParameters.model().map(CorrectTextModel.class);
            if (correctTextModel.isWithAudio()) {
                outputGateway.sendPlainMessage(publishHookParameters.chatId(), "Generating a voiceover for corrected text...");
                byte[] voicedCorrectedText = openAiClient.createSpeech(correctedTextCache.getCorrectedText().orElseThrow().correctedText());
                outputGateway.sendPlainMessage(publishHookParameters.chatId(), "The voiceover is generated... Publishing it as well!");
                outputGateway.sendCorrectedTextAudio(
                        publishHookParameters.groupId(),
                        new ByteArrayInputStream(voicedCorrectedText)
                );
            }
        };
    }

    @Override
    public Runnable clearCache() {
        return correctedTextCache::clear;
    }

    @Override
    public void provideDynamicPrompt(AbstractCommandModel<?> currentModelState, CommandParameters parameters) {
        final String paragraphSeparator = "\n\n";

        final CorrectTextModel correctTextModel = currentModelState.map(CorrectTextModel.class);

        outputGateway.sendPlainMessage(parameters.chatId(), "Setting up the preview...");

        List<String> inputParagraphs = Arrays.stream(correctTextModel
                        .getTextWithCorrections()
                        .split(paragraphSeparator))
                .toList();

        List<String> originalParagraphs = inputParagraphs
                .stream()
                .map(CorrectionRenderer::create)
                .map(CorrectionRenderer::getSanitizedNonCorrectedText)
                .toList();

        List<String> correctedParagraphs = inputParagraphs
                .stream()
                .map(CorrectionRenderer::create)
                .map(CorrectionRenderer::getSanitizedTextWithCorrections)
                .toList();

        String correctText = inputParagraphs
                .stream()
                .map(CorrectionRenderer::create)
                .map(CorrectionRenderer::getSanitizedCorrectPlainText)
                .collect(Collectors.joining("\n"));

        CorrectedTextDocumentMetadata originalParagraphsDocumentMetadata = new CorrectedTextDocumentMetadata(
                correctTextModel.getTopic(),
                CorrectedTextDocumentMetadata.TextType.TEXT,
                originalParagraphs
        );

        CorrectedTextDocumentMetadata correctedParagraphsDocumentMetadata = new CorrectedTextDocumentMetadata(
                correctTextModel.getTopic(),
                CorrectedTextDocumentMetadata.TextType.CORRECTION,
                correctedParagraphs
        );


        ExerciseDocument textWithMistakesDocument = correctedTextDocumentPipe.pipe(originalParagraphsDocumentMetadata);
        ExerciseDocument textWithMistakesAndCorrectionsDocument = correctedTextDocumentPipe.pipe(correctedParagraphsDocumentMetadata);

        outputGateway.sendPlainMessage(parameters.chatId(), "Sending the preview...");
        outputGateway.sendCorrectedText(parameters.chatId(), textWithMistakesDocument, textWithMistakesAndCorrectionsDocument);

        var correctedTextContainer = new CorrectedTextCache.CorrectedTexContainer(
                correctText,
                textWithMistakesDocument,
                textWithMistakesAndCorrectionsDocument
        );

        correctedTextCache.saveCorrectedText(correctedTextContainer);
    }

    @Override
    public AbstractCommandModel<?> supportedModelWithMapping() {
        return new CorrectTextModel()
                .addMapping(TOPIC, CorrectTextModel::setTopic)
                .addMapping(CORRECTED_TEXT, CorrectTextModel::setTextWithCorrections)
                .addMapping(SHOULD_DO, CorrectTextModel::parseValue)
                .addMapping(WITH_AUDIO, CorrectTextModel::withAudio);
    }
}
