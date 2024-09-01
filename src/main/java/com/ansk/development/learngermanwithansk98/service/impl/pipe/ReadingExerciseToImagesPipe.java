package com.ansk.development.learngermanwithansk98.service.impl.pipe;

import com.ansk.development.learngermanwithansk98.service.api.IConverterPipe;
import com.ansk.development.learngermanwithansk98.service.model.output.Images;
import com.ansk.development.learngermanwithansk98.service.model.output.ReadingExercise;
import com.itextpdf.styledxmlparser.jsoup.nodes.Document;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Component
public class ReadingExerciseToImagesPipe extends AbstractObjectToHtmlPipe<ReadingExercise> implements IConverterPipe<ReadingExercise, Images> {

    private final SpringTemplateEngine templateEngine;

    public ReadingExerciseToImagesPipe(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public Images pipe(ReadingExercise readingExercise) {
        Document html = abstractPipe("reading_exercise_template", "readingExercise", readingExercise).apply(templateEngine);
        PDDocument pdfDocument = new HtmlToPdfPipe().pipe(html);
        return new PdfToImagePipe().pipe(pdfDocument);
    }
}
