package com.ansk.development.learngermanwithansk98.service.model.output;

import java.util.List;

/**
 * This record encapsulates metadata related to rendering a document with text and its corrected version,
 *
 * @param topic      topic
 * @param mode       mode if it is an original text or the corrected one
 * @param paragraphs paragraphs of the text
 */
public record WritingCorrectionDocumentMetadata(String topic, Mode mode, List<String> paragraphs) {
    public enum Mode {
        TEXT("text"),
        CORRECTION("correction");

        private final String mode;

        Mode(String mode) {
            this.mode = mode;
        }

        public String mode() {
            return mode;
        }
    }
}
