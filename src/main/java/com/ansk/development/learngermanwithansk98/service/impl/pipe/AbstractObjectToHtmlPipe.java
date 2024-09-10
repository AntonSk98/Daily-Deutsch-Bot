package com.ansk.development.learngermanwithansk98.service.impl.pipe;

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
 * @author Anton Skripin
 */
public abstract class AbstractObjectToHtmlPipe<T> {

    /**
     * Generates a function that, when provided with a {@link SpringTemplateEngine},
     * produces an HTML {@link Document} by embedding an object into the given HTML template.
     *
     * @param template the name or path of the HTML template to be processed
     * @param context  the variable name to be used in the template for the embedded object
     * @param object   the object to embed into the HTML template under the given context
     * @return a function that accepts a {@link SpringTemplateEngine} and returns a {@link Document}
     */
    public Function<SpringTemplateEngine, Document> abstractPipe(String template, String context, T object) {
        Context ctx = new Context();
        ctx.setVariable(context, object);
        return springTemplateEngine -> {
            String html = springTemplateEngine.process(template, ctx);
            return Jsoup.parse(html, EMPTY, Parser.htmlParser());
        };
    }
}
