package com.ansk.development.learngermanwithansk98.service.impl.pipe;

import com.ansk.development.learngermanwithansk98.service.api.IConverterPipe;
import com.ansk.development.learngermanwithansk98.service.model.WordCard;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.styledxmlparser.jsoup.nodes.Document;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Pipe that transforms html into pdf.
 *
 * @author Anton Skripin
 */
public class HtmlToPdfPipe implements IConverterPipe<Document, PDDocument> {

    @Override
    public PDDocument pipe(Document html) {
        String htmlString = html.html();
        return convertToPdf(new ByteArrayInputStream(htmlString.getBytes()));
    }

    private PDDocument convertToPdf(InputStream htmlInputStream) {
        try {
            ByteArrayOutputStream pdfOutputStream = new ByteArrayOutputStream();
            PdfDocument pdfDocument = new PdfDocument(new PdfWriter(pdfOutputStream));
            pdfDocument.setDefaultPageSize(PageSize.A4);
            HtmlConverter.convertToPdf(htmlInputStream, pdfDocument);
            return Loader.loadPDF(pdfOutputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
