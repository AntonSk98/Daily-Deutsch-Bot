package com.ansk.development.learngermanwithansk98.service.impl.command.writing;

import com.ansk.development.learngermanwithansk98.config.CommandsConfiguration;
import com.ansk.development.learngermanwithansk98.gateway.telegram.ITelegramOutputGateway;
import com.ansk.development.learngermanwithansk98.repository.CommandCache;
import com.ansk.development.learngermanwithansk98.service.impl.command.AbstractCommandProcessor;
import com.ansk.development.learngermanwithansk98.service.impl.command.writing.correction.CorrectionRenderer;
import com.ansk.development.learngermanwithansk98.service.impl.pipe.WritingCorrectionDocumentPipe;
import com.ansk.development.learngermanwithansk98.service.model.Command;
import com.ansk.development.learngermanwithansk98.service.model.input.AbstractCommandModel;
import com.ansk.development.learngermanwithansk98.service.model.input.CommandParameters;
import com.ansk.development.learngermanwithansk98.service.model.input.CorrectingWritingExerciseModel;
import com.ansk.development.learngermanwithansk98.service.model.output.ExerciseDocument;
import com.ansk.development.learngermanwithansk98.service.model.output.WritingCorrectionDocumentMetadata;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Service responsible for processing a user's writing exercise and returning both the
 * original text and its corrected version. This service is used
 *
 * @author Anton Skripin
 */
@Service
public class CorrectWritingExercise extends AbstractCommandProcessor {

    private final ITelegramOutputGateway outputGateway;
    private final WritingCorrectionDocumentPipe writingCorrectionDocumentPipe;

    /**
     * Constructor.
     *
     * @param commandsConfiguration         See {@link CommandsConfiguration}
     * @param telegramOutputGateway         See {@link ITelegramOutputGateway}
     * @param commandCache                  See {@link CommandCache}
     * @param writingCorrectionDocumentPipe See {@link WritingCorrectionDocumentPipe}
     */
    protected CorrectWritingExercise(CommandsConfiguration commandsConfiguration,
                                     ITelegramOutputGateway telegramOutputGateway,
                                     CommandCache commandCache,
                                     WritingCorrectionDocumentPipe writingCorrectionDocumentPipe) {
        super(commandsConfiguration, telegramOutputGateway, commandCache);
        this.outputGateway = telegramOutputGateway;
        this.writingCorrectionDocumentPipe = writingCorrectionDocumentPipe;
    }

    @Override
    public Command supportedCommand() {
        return Command.CORRECTED_WRITING;
    }

    @Override
    public void applyCommandModel(AbstractCommandModel<?> model, CommandParameters parameters) {
        final String paragraphSeparator = "\n\n";

        outputGateway.sendPlainMessage(parameters.chatId(), "Processing the input...");

        CorrectingWritingExerciseModel correctingWritingExerciseModel = model.map(CorrectingWritingExerciseModel.class);

        List<String> inputParagraphs = Arrays.stream(correctingWritingExerciseModel
                        .getCorrectedExercise()
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

        WritingCorrectionDocumentMetadata originalParagraphsDocumentMetadata = new WritingCorrectionDocumentMetadata(
                correctingWritingExerciseModel.getTopic(),
                WritingCorrectionDocumentMetadata.Mode.TEXT,
                originalParagraphs
        );

        WritingCorrectionDocumentMetadata correctedParagraphsDocumentMetadata = new WritingCorrectionDocumentMetadata(
                correctingWritingExerciseModel.getTopic(),
                WritingCorrectionDocumentMetadata.Mode.CORRECTION,
                correctedParagraphs
        );

        outputGateway.sendPlainMessage(parameters.chatId(), "Converting it all into documents...");
        ExerciseDocument originalTextDocument = writingCorrectionDocumentPipe.pipe(originalParagraphsDocumentMetadata);
        ExerciseDocument correctedTextDocument = writingCorrectionDocumentPipe.pipe(correctedParagraphsDocumentMetadata);


        outputGateway.sendPlainMessage(parameters.chatId(), "Sending the documents...");
        outputGateway.sendCorrectedWriting(parameters.chatId(), originalTextDocument, correctedTextDocument);
    }

    @Override
    public AbstractCommandModel<?> supportedModelWithMapping() {
        return new CorrectingWritingExerciseModel()
                .addMapping("topic", CorrectingWritingExerciseModel::setTopic)
                .addMapping("corrected_writing", CorrectingWritingExerciseModel::setCorrectedExercise);
    }
}
