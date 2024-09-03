package com.ansk.development.learngermanwithansk98.service.impl.pipe;

import com.ansk.development.learngermanwithansk98.service.api.IConverterPipe;
import com.ansk.development.learngermanwithansk98.service.model.output.Images;
import com.ansk.development.learngermanwithansk98.service.model.output.ReadingExercise;
import com.itextpdf.styledxmlparser.jsoup.nodes.Document;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring6.SpringTemplateEngine;

/**
 * Pipe that convert a {@link ReadingExercise.Document} to {@link Images}.
 *
 * @author Anton Skripin
 */
@Component
public class ReadingExerciseDocumentPipe extends AbstractObjectToHtmlPipe<ReadingExercise.Document> implements IConverterPipe<ReadingExercise.Document, Images> {

    private final SpringTemplateEngine springTemplateEngine;

    public ReadingExerciseDocumentPipe(SpringTemplateEngine springTemplateEngine) {
        this.springTemplateEngine = springTemplateEngine;
    }

    @Override
    public Images pipe(ReadingExercise.Document document) {
        Document html = abstractPipe("reading_exercise_template", "readingExercise", document).apply(springTemplateEngine);
        PDDocument pdfDocument = new HtmlToPdfPipe().pipe(html);
        return new PdfToImagePipe().pipe(pdfDocument);
    }
}
