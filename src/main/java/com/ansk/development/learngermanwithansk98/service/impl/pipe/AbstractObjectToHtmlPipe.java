package com.ansk.development.learngermanwithansk98.service.impl.pipe;

import com.ansk.development.learngermanwithansk98.service.api.IConverterPipe;
import com.itextpdf.styledxmlparser.jsoup.Jsoup;
import com.itextpdf.styledxmlparser.jsoup.nodes.Document;
import com.itextpdf.styledxmlparser.jsoup.parser.Parser;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.function.Function;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Abstract pipe that provides a utility function to convert an object to html page.
 *
 * @param <T> object type
 *
 * @author Anton Skripin
 */
public abstract class AbstractObjectToHtmlPipe<T> {

    public Function<SpringTemplateEngine, Document> abstractPipe(String template, String contextName, T object) {
        Context ctx = new Context();
        ctx.setVariable(contextName, object);
        return springTemplateEngine -> {
            String html = springTemplateEngine.process(template, ctx);
            return Jsoup.parse(html, EMPTY, Parser.htmlParser());
        };
    }
}
