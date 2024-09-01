package com.ansk.development.learngermanwithansk98.service.model.output;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Images {

    private final List<byte[]> binaryImages = new ArrayList<>();

    private Images() {
    }

    public static Images of() {
        return new Images();
    }

    public Images addBinaryImage(byte[] binaryImage) {
        binaryImages.add(binaryImage);
        return this;
    }

    public List<byte[]> getBinaryImages() {
        return Collections.unmodifiableList(binaryImages);
    }
}
