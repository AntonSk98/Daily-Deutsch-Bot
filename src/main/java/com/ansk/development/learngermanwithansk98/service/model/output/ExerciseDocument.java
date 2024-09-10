package com.ansk.development.learngermanwithansk98.service.model.output;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A class representing a document consisting of multiple binary image pages.
 * The {@code ExerciseDocument} class provides functionality to add pages and
 * retrieve an immutable list of the pages.
 *
 * @author Anton Skripin
 */
public class ExerciseDocument {

    private final List<byte[]> pages = new ArrayList<>();

    private ExerciseDocument() {
    }

    /**
     * Creates and returns a new instance of {@code ExerciseDocument}.
     *
     * @return a new instance of {@code ExerciseDocument}
     */
    public static ExerciseDocument of() {
        return new ExerciseDocument();
    }

    /**
     * Adds a binary image page to the document.
     *
     * @param binaryImage the byte array representing the image to be added as a page
     * @return the current instance of {@code ExerciseDocument} with the added page
     */
    public ExerciseDocument addPage(byte[] binaryImage) {
        pages.add(binaryImage);
        return this;
    }

    /**
     * Retrieves an unmodifiable list of the pages in the document.
     *
     * @return an unmodifiable {@link List} of byte arrays representing the pages
     */
    public List<byte[]> pages() {
        return Collections.unmodifiableList(pages);
    }

    /**
     * Checks if the document contains exactly one page.
     *
     * @return {@code true} if the document contains one page, {@code false} otherwise
     */
    public boolean onePage() {
        return pages.size() == 1;
    }
}
