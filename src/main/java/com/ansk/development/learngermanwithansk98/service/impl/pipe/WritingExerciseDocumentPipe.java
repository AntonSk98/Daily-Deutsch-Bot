package com.ansk.development.learngermanwithansk98.service.impl.pipe;

import com.ansk.development.learngermanwithansk98.config.DailyDeutschBotConfiguration;
import com.ansk.development.learngermanwithansk98.service.api.IConverterPipe;
import com.ansk.development.learngermanwithansk98.service.model.output.ExerciseDocument;
import com.ansk.development.learngermanwithansk98.service.model.output.WritingExercise;
import com.itextpdf.styledxmlparser.jsoup.nodes.Document;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring6.SpringTemplateEngine;


/**
 * Pipe that converts {@link WritingExercise} to {@link ExerciseDocument}.
 *
 * @author Anton Skripin
 */
@Component
public class WritingExerciseDocumentPipe extends AbstractObjectToHtmlPipe<WritingExercise.Output> implements IConverterPipe<WritingExercise.Output, ExerciseDocument> {

    /**
     * Constructor.
     *
     * @param configuration        See {@link DailyDeutschBotConfiguration}
     * @param springTemplateEngine See {@link SpringTemplateEngine}
     */
    protected WritingExerciseDocumentPipe(DailyDeutschBotConfiguration configuration, SpringTemplateEngine springTemplateEngine) {
        super(configuration, springTemplateEngine);
    }


    @Override
    public ExerciseDocument pipe(WritingExercise.Output writingExercise) {
        Document html = abstractPipe("writing_exercise_template.html", "writingExercise", writingExercise);
        PDDocument pdfDocument = new HtmlToPdfPipe().pipe(html);
        return new PdfToImagePipe().pipe(pdfDocument);
    }
}
