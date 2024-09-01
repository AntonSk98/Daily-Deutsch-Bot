package com.ansk.development.learngermanwithansk98.service.impl.pipe;

import com.ansk.development.learngermanwithansk98.service.api.IConverterPipe;
import com.ansk.development.learngermanwithansk98.service.model.output.Images;
import com.ansk.development.learngermanwithansk98.service.model.output.WordCard;
import com.itextpdf.styledxmlparser.jsoup.nodes.Document;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

/**
 * Defines what {@link IConverterPipe}s and in which order must be applied to transform a {@link WordCard} to {@link Images}.
 *
 * @author Anton Skripin
 */
@Service
public class CardToImagesConverterPipe extends AbstractObjectToHtmlPipe<WordCard> implements IConverterPipe<WordCard, Images> {

    private final SpringTemplateEngine springTemplateEngine;

    public CardToImagesConverterPipe(SpringTemplateEngine springTemplateEngine) {
        this.springTemplateEngine = springTemplateEngine;
    }

    @Override
    public Images pipe(WordCard wordCard) {
        Document html = abstractPipe("vocab_template", "wordCard", wordCard).apply(springTemplateEngine);
        PDDocument pdfDocument = new HtmlToPdfPipe().pipe(html);
        return new PdfToImagePipe().pipe(pdfDocument);
    }
}
