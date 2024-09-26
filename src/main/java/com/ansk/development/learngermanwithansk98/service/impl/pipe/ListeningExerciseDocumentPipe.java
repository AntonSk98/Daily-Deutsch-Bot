package com.ansk.development.learngermanwithansk98.service.impl.pipe;

import com.ansk.development.learngermanwithansk98.config.DailyDeutschBotConfiguration;
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

    private final HtmlToPdfPipe htmlToPdfPipe;

    /**
     * Constructor.
     *
     * @param configuration        See {@link DailyDeutschBotConfiguration}
     * @param springTemplateEngine See {@link SpringTemplateEngine}
     */
    protected ListeningExerciseDocumentPipe(DailyDeutschBotConfiguration configuration,
                                            SpringTemplateEngine springTemplateEngine) {
        super(configuration, springTemplateEngine);
        this.htmlToPdfPipe = new HtmlToPdfPipe(configuration);
    }

    @Override
    public ExerciseDocument pipe(ListeningExercise.Document exercise) {
        Document html = abstractPipe("listening_exercise_template", "listeningExercise", exercise);
        PDDocument pdfDocument = htmlToPdfPipe.pipe(html);
        return new PdfToImagePipe().pipe(pdfDocument);
    }
}
