package com.ansk.development.learngermanwithansk98.service.impl.command.writing.correction;

import com.itextpdf.styledxmlparser.jsoup.Jsoup;
import com.itextpdf.styledxmlparser.jsoup.safety.Safelist;

/**
 * This class is responsible for sanitizing HTML content by removing any unsafe elements or attributes while allowing specific safe tags and attributes.
 *
 * @author Anton Skripin
 */
class RenderedSanitizer {

    /**
     * Sanitizes a text with html blocks.
     *
     * @param unsafeHtml potentially unsafe html
     * @return sanitized html
     */
    String sanitize(String unsafeHtml) {
        Safelist safelist = Safelist.basic().addAttributes("span", "class");
        return Jsoup.clean(unsafeHtml, safelist);
    }
}
