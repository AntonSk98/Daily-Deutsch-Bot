package com.ansk.development.learngermanwithansk98.service.impl.pipe;

import com.ansk.development.learngermanwithansk98.service.api.IConverterPipe;
import com.ansk.development.learngermanwithansk98.service.model.Images;
import com.ansk.development.learngermanwithansk98.service.model.WordCard;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

/**
 * Defines what {@link IConverterPipe}s and in which order must be applied to transform a {@link WordCard} to {@link Images}.
 *
 * @author Anton Skripin
 */
@Service
public class CardToImagesConverterPipe implements IConverterPipe<WordCard, Images> {

    private final CardToHtmlPipe cardToHtmlPipe;
    private final HtmlToPdfPipe htmlToPdfPipe;
    private final PdfToImagePipe pdfToImagesPipe;

    public CardToImagesConverterPipe(SpringTemplateEngine springTemplateEngine) {
        cardToHtmlPipe = new CardToHtmlPipe(springTemplateEngine);
        htmlToPdfPipe = new HtmlToPdfPipe();
        pdfToImagesPipe = new PdfToImagePipe();
    }

    @Override
    public Images pipe(WordCard wordCard) {
        return pdfToImagesPipe.pipe(
                htmlToPdfPipe.pipe(
                        cardToHtmlPipe.pipe(wordCard)
                )
        );
    }
}
