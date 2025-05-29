package com.ansk.development.learngermanwithansk98.service.impl.pipe;

import com.ansk.development.learngermanwithansk98.config.DailyDeutschBotConfiguration;
import com.ansk.development.learngermanwithansk98.service.api.IConverterPipe;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.resolver.font.DefaultFontProvider;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.font.FontProvider;
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

    private final String fontsFolder;

    public HtmlToPdfPipe(DailyDeutschBotConfiguration configuration) {
        this.fontsFolder = configuration.resourceFolder() + "/fonts";
    }

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

            // Set up font resolver to use Helvetica
            FontProvider fontProvider = new DefaultFontProvider(false, false, false);
            fontProvider.addDirectory(fontsFolder);
            System.out.println(fontsFolder);

            HtmlConverter.convertToPdf(htmlInputStream, pdfDocument, new ConverterProperties().setFontProvider(fontProvider));

            return Loader.loadPDF(pdfOutputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
