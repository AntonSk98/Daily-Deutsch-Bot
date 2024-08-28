package com.ansk.development.learngermanwithansk98.service.impl.pipe;

import com.ansk.development.learngermanwithansk98.service.api.IConverterPipe;
import com.ansk.development.learngermanwithansk98.service.model.Images;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Pipe that transform a pdf into an {@link Images}.
 *
 * @author Anton Skripin
 */
public class PdfToImagePipe implements IConverterPipe<PDDocument, Images> {


    private static final String IMAGE_FORMAT = "PNG";
    private static final Integer IMAGE_DPI = 300;

    @Override
    public Images pipe(PDDocument pdfDocument) {
        Images binaryImagesPipeValue = Images.of();
        PDFRenderer pdfRenderer = new PDFRenderer(pdfDocument);
        int pageCount = pdfDocument.getNumberOfPages();
        for (int page = 0; page < pageCount; ++page) {
            BufferedImage image = toBufferedImage(pdfRenderer, page);
            byte[] binaryImage = toBinaryImage(image, IMAGE_FORMAT);
            binaryImagesPipeValue.addBinaryImage(binaryImage);
        }
        return binaryImagesPipeValue;
    }

    private byte[] toBinaryImage(BufferedImage image, String format) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ImageIO.write(image, format, outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert image to byte array", e);
        }
    }

    private BufferedImage toBufferedImage(PDFRenderer pdfRenderer, int page) {
        try {
            return pdfRenderer.renderImageWithDPI(page, IMAGE_DPI, ImageType.BGR);
        } catch (IOException e) {
            throw new RuntimeException("Failed to convert to buffered image", e);
        }
    }
}
