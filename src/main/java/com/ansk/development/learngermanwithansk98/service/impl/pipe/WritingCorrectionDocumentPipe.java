package com.ansk.development.learngermanwithansk98.service.impl.pipe;

import com.ansk.development.learngermanwithansk98.config.DailyDeutschBotConfiguration;
import com.ansk.development.learngermanwithansk98.service.api.IConverterPipe;
import com.ansk.development.learngermanwithansk98.service.model.output.ExerciseDocument;
import com.ansk.development.learngermanwithansk98.service.model.output.WritingCorrectionDocumentMetadata;
import com.itextpdf.styledxmlparser.jsoup.nodes.Document;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring6.SpringTemplateEngine;

/**
 * Pipe that converts {@link WritingCorrectionDocumentMetadata} to {@link ExerciseDocument}.
 *
 * @author Anton Skripin
 */
@Component
public class WritingCorrectionDocumentPipe extends AbstractObjectToHtmlPipe<WritingCorrectionDocumentMetadata> implements IConverterPipe<WritingCorrectionDocumentMetadata, ExerciseDocument> {

    protected WritingCorrectionDocumentPipe(DailyDeutschBotConfiguration configuration,
                                            SpringTemplateEngine springTemplateEngine) {
        super(configuration, springTemplateEngine);
    }

    @Override
    public ExerciseDocument pipe(WritingCorrectionDocumentMetadata writingCorrectionDocumentMetadata) {
        Document html = abstractPipe("writing_correction_template", "writingCorrection", writingCorrectionDocumentMetadata);
        PDDocument pdfDocument = new HtmlToPdfPipe().pipe(html);
        return new PdfToImagePipe().pipe(pdfDocument);
    }
}
