package com.ansk.development.learngermanwithansk98.service.impl.pipe;

import com.ansk.development.learngermanwithansk98.config.DailyDeutschBotConfiguration;
import com.ansk.development.learngermanwithansk98.service.api.IConverterPipe;
import com.ansk.development.learngermanwithansk98.service.model.output.ExerciseDocument;
import com.ansk.development.learngermanwithansk98.service.model.output.ReadingExercise;
import com.itextpdf.styledxmlparser.jsoup.nodes.Document;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring6.SpringTemplateEngine;

/**
 * Pipe that convert a {@link ReadingExercise.Document} to {@link ExerciseDocument}.
 *
 * @author Anton Skripin
 */
@Component
public class ReadingExerciseDocumentPipe extends AbstractObjectToHtmlPipe<ReadingExercise.Document> implements IConverterPipe<ReadingExercise.Document, ExerciseDocument> {

    /**
     * Constructor
     *
     * @param configuration        See {@link DailyDeutschBotConfiguration}
     * @param springTemplateEngine See {@link SpringTemplateEngine}
     */
    protected ReadingExerciseDocumentPipe(DailyDeutschBotConfiguration configuration, SpringTemplateEngine springTemplateEngine) {
        super(configuration, springTemplateEngine);
    }


    @Override
    public ExerciseDocument pipe(ReadingExercise.Document document) {
        Document html = abstractPipe("reading_exercise_template", "readingExercise", document);
        PDDocument pdfDocument = new HtmlToPdfPipe().pipe(html);
        return new PdfToImagePipe().pipe(pdfDocument);
    }
}
