package com.ansk.development.learngermanwithansk98.service.impl.pipe;

import com.ansk.development.learngermanwithansk98.service.api.IConverterPipe;
import com.ansk.development.learngermanwithansk98.service.model.output.ExerciseDocument;
import com.ansk.development.learngermanwithansk98.service.model.output.ListeningExercise;
import com.itextpdf.styledxmlparser.jsoup.nodes.Document;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring6.SpringTemplateEngine;

/**
 * Pipe that convert a {@link ListeningExercise.Output} to {@link ExerciseDocument}.
 *
 * @author Anton Skripin
 */
@Component
public class ListeningExerciseDocumentPipe extends AbstractObjectToHtmlPipe<ListeningExercise.Document> implements IConverterPipe<ListeningExercise.Document, ExerciseDocument> {

    private final SpringTemplateEngine springTemplateEngine;

    /**
     * Constructor.
     *
     * @param springTemplateEngine See {@link SpringTemplateEngine}
     */
    public ListeningExerciseDocumentPipe(SpringTemplateEngine springTemplateEngine) {
        this.springTemplateEngine = springTemplateEngine;
    }

    @Override
    public ExerciseDocument pipe(ListeningExercise.Document exercise) {
        Document html = abstractPipe("listening_exercise_template", "listeningExercise", exercise).apply(springTemplateEngine);
        PDDocument pdfDocument = new HtmlToPdfPipe().pipe(html);
        return new PdfToImagePipe().pipe(pdfDocument);
    }
}
