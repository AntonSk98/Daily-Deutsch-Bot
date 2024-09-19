package com.ansk.development.learngermanwithansk98.service.impl.pipe;

import com.ansk.development.learngermanwithansk98.config.DailyDeutschBotConfiguration;
import com.ansk.development.learngermanwithansk98.service.api.IConverterPipe;
import com.ansk.development.learngermanwithansk98.service.model.output.CorrectedTextDocumentMetadata;
import com.ansk.development.learngermanwithansk98.service.model.output.ExerciseDocument;
import com.itextpdf.styledxmlparser.jsoup.nodes.Document;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring6.SpringTemplateEngine;

/**
 * Pipe that converts {@link CorrectedTextDocumentMetadata} to {@link ExerciseDocument}.
 *
 * @author Anton Skripin
 */
@Component
public class CorrectedTextDocumentPipe extends AbstractObjectToHtmlPipe<CorrectedTextDocumentMetadata> implements IConverterPipe<CorrectedTextDocumentMetadata, ExerciseDocument> {

    /**
     * Constructor.
     *
     * @param configuration        See {@link DailyDeutschBotConfiguration}
     * @param springTemplateEngine See {@link SpringTemplateEngine}
     */
    protected CorrectedTextDocumentPipe(DailyDeutschBotConfiguration configuration,
                                        SpringTemplateEngine springTemplateEngine) {
        super(configuration, springTemplateEngine);
    }

    @Override
    public ExerciseDocument pipe(CorrectedTextDocumentMetadata correctedTextDocumentMetadata) {
        Document html = abstractPipe("writing_correction_template", "writingCorrection", correctedTextDocumentMetadata);
        PDDocument pdfDocument = new HtmlToPdfPipe().pipe(html);
        return new PdfToImagePipe().pipe(pdfDocument);
    }
}
