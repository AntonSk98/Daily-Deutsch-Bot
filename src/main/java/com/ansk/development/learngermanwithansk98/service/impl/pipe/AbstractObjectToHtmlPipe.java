package com.ansk.development.learngermanwithansk98.service.impl.pipe;

import com.ansk.development.learngermanwithansk98.config.DailyDeutschBotConfiguration;
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

    private final DailyDeutschBotConfiguration botConfiguration;
    private final SpringTemplateEngine springTemplateEngine;

    protected AbstractObjectToHtmlPipe(DailyDeutschBotConfiguration configuration,
                                       SpringTemplateEngine springTemplateEngine) {
        this.botConfiguration = configuration;
        this.springTemplateEngine = springTemplateEngine;
    }

    /**
     * Generates a {@link Document} using the context information and {@link SpringTemplateEngine} as a renderer.
     *
     * @param template the name or path of the HTML template to be processed
     * @param context  the variable name to be used in the template for the embedded object
     * @param object   the object to embed into the HTML template under the given context
     * @return html document
     */
    public Document abstractPipe(String template, String context, T object) {
        Context ctx = new Context();
        ctx.setVariable("resources", botConfiguration.resourceFolder());
        ctx.setVariable(context, object);

        String html = springTemplateEngine.process(template, ctx);
        return Jsoup.parse(html, EMPTY, Parser.htmlParser());
    }
}
