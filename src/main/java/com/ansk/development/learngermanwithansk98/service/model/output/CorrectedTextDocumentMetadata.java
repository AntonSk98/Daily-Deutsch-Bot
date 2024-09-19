package com.ansk.development.learngermanwithansk98.service.model.output;

import java.util.List;

/**
 * This record encapsulates metadata related to rendering a document with text and its corrected version,
 *
 * @param topic      topic
 * @param textType   mode if it is an original text or the corrected one
 * @param paragraphs paragraphs of the text
 */
public record CorrectedTextDocumentMetadata(String topic, TextType textType, List<String> paragraphs) {

    /**
     * Text type
     */
    public enum TextType {
        TEXT("text"),
        CORRECTION("correction");

        private final String type;

        TextType(String type) {
            this.type = type;
        }

        public String mode() {
            return type;
        }
    }
}
