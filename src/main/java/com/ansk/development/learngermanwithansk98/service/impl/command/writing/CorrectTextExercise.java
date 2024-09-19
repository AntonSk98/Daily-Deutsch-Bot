package com.ansk.development.learngermanwithansk98.service.impl.command.writing;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.gateway.telegram.ITelegramOutputGateway;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.repository.CorrectedTextCache;
import com.ansk.development.learngermanwithansk98.service.impl.command.AbstractCommandProcessor;
import com.ansk.development.learngermanwithansk98.service.impl.command.writing.correction.CorrectionRenderer;
import com.ansk.development.learngermanwithansk98.service.impl.pipe.CorrectedTextDocumentPipe;
import com.ansk.development.learngermanwithansk98.service.model.Command;
import com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel;
import com.ansk.development.learngermanwithansk98.service.model.input.CommandParameters;
import com.ansk.development.learngermanwithansk98.service.model.input.CorrectTextModel;
import com.ansk.development.learngermanwithansk98.service.model.output.CorrectedTextDocumentMetadata;
import com.ansk.development.learngermanwithansk98.service.model.output.ExerciseDocument;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

import static com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel.Properties.*;

/**
 * Service responsible for processing a text and returning both the original text and its corrected version.
 *
 * @author Anton Skripin
 */
@Service
public class CorrectTextExercise extends AbstractCommandProcessor {

    private final ITelegramOutputGateway outputGateway;
    private final CorrectedTextDocumentPipe correctedTextDocumentPipe;
    private final CorrectedTextCache correctedTextCache;

    /**
     * Constructor.
     *
     * @param commandsConfiguration     See {@link CommandsConfiguration}
     * @param telegramOutputGateway     See {@link ITelegramOutputGateway}
     * @param correctedTextDocumentPipe See {@link CorrectedTextDocumentPipe}
     */
    protected CorrectTextExercise(CommandsConfiguration commandsConfiguration,
                                  ITelegramOutputGateway telegramOutputGateway,
                                  CommandCache commandCache,
                                  CorrectedTextDocumentPipe correctedTextDocumentPipe,
                                  CorrectedTextCache correctedTextCache) {
        super(commandsConfiguration, telegramOutputGateway, commandCache);
        this.outputGateway = telegramOutputGateway;
        this.correctedTextDocumentPipe = correctedTextDocumentPipe;
        this.correctedTextCache = correctedTextCache;
    }

    @Override
    public Command supportedCommand() {
        return Command.CORRECT_TEXT;
    }

    @Override
    public void applyCommandModel(AbstractCommandModel<?> model, CommandParameters parameters) {
        final CorrectTextModel correctTextModel = model.map(CorrectTextModel.class);
        if (correctTextModel.shouldPublish()) {
            outputGateway.sendPlainMessage(parameters.chatId(), "Publishing...");
        }
        correctedTextCache.clear();
        outputGateway.sendPlainMessage(parameters.chatId(), "Exercise cache evicted...");
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
                .map(CorrectionRenderer::getSanitizedOriginalTextSplitByParagraphs)
                .toList();

        List<String> correctedParagraphs = inputParagraphs
                .stream()
                .map(CorrectionRenderer::create)
                .map(CorrectionRenderer::getSanitizedCorrectedTextSplitByParagraphs)
                .toList();

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


        ExerciseDocument originalTextDocument = correctedTextDocumentPipe.pipe(originalParagraphsDocumentMetadata);
        ExerciseDocument correctedTextDocument = correctedTextDocumentPipe.pipe(correctedParagraphsDocumentMetadata);

        outputGateway.sendPlainMessage(parameters.chatId(), "Sending the preview...");
        outputGateway.sendCorrectedText(parameters.chatId(), originalTextDocument, correctedTextDocument);

        correctedTextCache.saveCorrectedText(new CorrectedTextCache.CorrectedTexContainer(originalTextDocument, correctedTextDocument));
    }

    @Override
    public AbstractCommandModel<?> supportedModelWithMapping() {
        return new CorrectTextModel()
                .addMapping(TOPIC, CorrectTextModel::setTopic)
                .addMapping(CORRECTED_TEXT, CorrectTextModel::setTextWithCorrections)
                .addMapping(SHOULD_PUBLISH_TEXT_AND_CORRECTION, (model, value) -> model.setShouldPublish(value.contains(APPROVE_PROMPT)));
    }
}
