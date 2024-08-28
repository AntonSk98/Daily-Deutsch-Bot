package com.ansk.development.learngermanwithansk98.service.impl.pipe;

import com.ansk.development.learngermanwithansk98.service.api.IConverterPipe;
import com.ansk.development.learngermanwithansk98.service.model.WordCard;
import com.itextpdf.styledxmlparser.jsoup.Jsoup;
import com.itextpdf.styledxmlparser.jsoup.nodes.Document;
import com.itextpdf.styledxmlparser.jsoup.parser.Parser;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Pipe that transforms {@link WordCard} to html.
 *
 * @author Anton Skripin
 */
public class CardToHtmlPipe implements IConverterPipe<WordCard, Document> {

    private final SpringTemplateEngine springTemplateEngine;

    public CardToHtmlPipe(SpringTemplateEngine springTemplateEngine) {
        this.springTemplateEngine = springTemplateEngine;
    }

    @Override
    public Document pipe(WordCard wordCard) {
        Context ctx = new Context();
        ctx.setVariable("wordCard", wordCard);
        String html = springTemplateEngine.process("vocab_template", ctx);
        return Jsoup.parse(html, EMPTY, Parser.htmlParser());
    }
}
