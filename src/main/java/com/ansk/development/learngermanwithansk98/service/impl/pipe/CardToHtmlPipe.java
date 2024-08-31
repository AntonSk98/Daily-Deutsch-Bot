package com.ansk.development.learngermanwithansk98.service.impl.pipe;

import com.ansk.development.learngermanwithansk98.service.model.WordCard;
import com.itextpdf.styledxmlparser.jsoup.nodes.Document;
import org.thymeleaf.spring6.SpringTemplateEngine;

/**
 * Pipe that transforms {@link WordCard} to html.
 *
 * @author Anton Skripin
 */
public class CardToHtmlPipe extends AbstractObjectToHtmlPipe<WordCard> {

    private final SpringTemplateEngine springTemplateEngine;

    public CardToHtmlPipe(SpringTemplateEngine springTemplateEngine) {
        this.springTemplateEngine = springTemplateEngine;
    }

    @Override
    public Document pipe(WordCard wordCard) {
        return abstractPipe("vocab_template", "wordCard", wordCard).apply(springTemplateEngine);
    }
}
