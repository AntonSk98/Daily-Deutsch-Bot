package com.ansk.development.learngermanwithansk98.service.impl.pipe;

import com.ansk.development.learngermanwithansk98.service.api.IConverterPipe;
import com.ansk.development.learngermanwithansk98.service.model.output.Images;
import com.ansk.development.learngermanwithansk98.service.model.output.WritingExercise;
import com.itextpdf.styledxmlparser.jsoup.nodes.Document;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring6.SpringTemplateEngine;


/**
 * Pipe that converts {@link WritingExercise} to {@link Images}.
 *
 * @author Anton Skripin
 */
@Component
public class WritingExerciseDocumentPipe extends AbstractObjectToHtmlPipe<WritingExercise> implements IConverterPipe<WritingExercise, Images> {

    private final SpringTemplateEngine springTemplateEngine;

    public WritingExerciseDocumentPipe(SpringTemplateEngine springTemplateEngine) {
        this.springTemplateEngine = springTemplateEngine;
    }

    @Override
    public Images pipe(WritingExercise writingExercise) {
        Document html = abstractPipe("writing_exercise_template.html", "writingExercise", writingExercise).apply(springTemplateEngine);
        PDDocument pdfDocument = new HtmlToPdfPipe().pipe(html);
        return new PdfToImagePipe().pipe(pdfDocument);
    }
}
