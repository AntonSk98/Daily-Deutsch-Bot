package com.ansk.development.learngermanwithansk98.service.impl.command.writing.correction;

import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * This class processes raw text blocks that represent corrections made to a user's
 * writing exercise. It supports two main tasks: extracting the original text
 * without corrections and generating a corrected version that highlights additions
 * and deletions using HTML tags.
 *
 * @author Anton Skripin
 */
public class CorrectionRenderer {

    private static final String ADDED_BLOCK_REGEX = "\\+\\+(.*?)\\+\\+";
    private static final String REMOVED_BLOCK_REGEX = "—(.*?)—";
    private static final String CAPTURED_TEXT = "$1";

    private static final String BLOCK_START_HTML = "<span class='%s'>";
    private static final String BLOCK_END_HTML = "</span>";

    private static final String REMOVED_BLOCK_HTML = String.format(BLOCK_START_HTML, "removed");
    private static final String ADDED_BLOCK_HTML = String.format(BLOCK_START_HTML, "added");

    private final String rawWritingBlock;
    private final RenderedSanitizer renderedSanitizer;


    private CorrectionRenderer(String rawWritingBlock) {
        this.rawWritingBlock = rawWritingBlock;
        this.renderedSanitizer = new RenderedSanitizer();
    }

    /**
     * Factory method to create a new {@link CorrectionRenderer} instance.
     *
     * @param inputText text with marked-up corrections.
     * @return A new instance of {@link CorrectionRenderer}.
     */
    public static CorrectionRenderer create(String inputText) {
        return new CorrectionRenderer(inputText);
    }

    /**
     * Returns original text that is sanitized.
     *
     * @return original text
     */
    public String getSanitizedOriginalTextSplitByParagraphs() {
        String originalParagraph = toOriginalTextBlock(rawWritingBlock);
        return renderedSanitizer.sanitize(originalParagraph);
    }

    /**
     * Returns text with corrections that is sanitized.
     *
     * @return corrected text
     */
    public String getSanitizedCorrectedTextSplitByParagraphs() {
        String correctedBlock = toCorrectedTextBlock(rawWritingBlock);
        return renderedSanitizer.sanitize(correctedBlock);
    }

    private String toOriginalTextBlock(String paragraph) {
        String noAdditionParagraph = RegExUtils.replaceAll(paragraph, ADDED_BLOCK_REGEX, "");
        String noAdditionAndNoDeletionParagraph = RegExUtils.replaceAll(noAdditionParagraph, REMOVED_BLOCK_REGEX, CAPTURED_TEXT);
        return StringUtils.normalizeSpace(noAdditionAndNoDeletionParagraph);
    }

    private String toCorrectedTextBlock(String paragraph) {
        String withAdditions = RegExUtils.replaceAll(paragraph, ADDED_BLOCK_REGEX, ADDED_BLOCK_HTML + CAPTURED_TEXT + BLOCK_END_HTML);
        String withAdditionsAndDeletions = RegExUtils.replaceAll(withAdditions, REMOVED_BLOCK_REGEX, REMOVED_BLOCK_HTML + CAPTURED_TEXT + BLOCK_END_HTML);
        return StringUtils.normalizeSpace(withAdditionsAndDeletions);
    }

}
