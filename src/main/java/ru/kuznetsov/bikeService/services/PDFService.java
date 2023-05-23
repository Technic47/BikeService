package ru.kuznetsov.bikeService.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;
import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;


@Component
public class PDFService {
    private PdfDocument document;
    private AbstractShowableEntity entity;
    private String imagePath;

    public PDFService newPDFDocument() {
        this.document = new PdfDocument();
        return this;
    }

    public PDFService addEntity(AbstractShowableEntity newEntity) {
        this.entity = newEntity;
        return this;
    }

    public PDFService addImage(String imgName) {
        this.imagePath = "IMG/preview/" + imgName;
        return this;
    }

    public void build() throws IOException, DocumentException, URISyntaxException {
        PdfWriter.getInstance(document, new FileOutputStream(entity.getName() + ".pdf"));

        document.open();

        Path path = Paths.get(ClassLoader.getSystemResource(this.imagePath).toURI());
        Image img = Image.getInstance(path.toAbsolutePath().toString());
        document.add(img);

        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        Chunk chunk = new Chunk(entity.getCredentials(), font);
        document.add(chunk);

        Paragraph name = new Paragraph(entity.getCredentials(), font);
        document.add(name);

        document.close();

    }
}
