package com.ansk.development.learngermanwithansk98.service.model.output;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExerciseDocument {

    private final List<byte[]> pages = new ArrayList<>();

    private ExerciseDocument() {
    }

    public static ExerciseDocument of() {
        return new ExerciseDocument();
    }

    public ExerciseDocument addPage(byte[] binaryImage) {
        pages.add(binaryImage);
        return this;
    }

    public List<byte[]> pages() {
        return Collections.unmodifiableList(pages);
    }

    public boolean onePage() {
        return pages.size() == 1;
    }
}
