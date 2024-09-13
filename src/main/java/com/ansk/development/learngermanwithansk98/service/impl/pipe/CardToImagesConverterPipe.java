package com.ansk.development.learngermanwithansk98.service.impl.pipe;

import com.ansk.development.learngermanwithansk98.config.DailyDeutschBotConfiguration;
import com.ansk.development.learngermanwithansk98.service.api.IConverterPipe;
import com.ansk.development.learngermanwithansk98.service.model.output.ExerciseDocument;
import com.ansk.development.learngermanwithansk98.service.model.output.WordCard;
import com.itextpdf.styledxmlparser.jsoup.nodes.Document;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

/**
 * Defines what {@link IConverterPipe}s and in which order must be applied to transform a {@link WordCard} to {@link ExerciseDocument}.
 *
 * @author Anton Skripin
 */
@Service
public class CardToImagesConverterPipe extends AbstractObjectToHtmlPipe<WordCard> implements IConverterPipe<WordCard, ExerciseDocument> {


    /**
     * Constructor.
     *
     * @param configuration        See {@link DailyDeutschBotConfiguration}
     * @param springTemplateEngine See {@link SpringTemplateEngine}
     */
    protected CardToImagesConverterPipe(DailyDeutschBotConfiguration configuration, SpringTemplateEngine springTemplateEngine) {
        super(configuration, springTemplateEngine);
    }


    @Override
    public ExerciseDocument pipe(WordCard wordCard) {
        Document html = abstractPipe("vocab_template", "wordCard", wordCard);
        PDDocument pdfDocument = new HtmlToPdfPipe().pipe(html);
        return new PdfToImagePipe().pipe(pdfDocument);
    }
}
