package ru.kuznetsov.bikeService.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;
import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static ru.kuznetsov.bikeService.config.SpringConfig.UPLOAD_PATH;


@Component
public class PDFService {
    // https://coderlessons.com/tutorials/raznoe/uznaite-itext/itext-kratkoe-rukovodstvo
    private Document document;
    private AbstractShowableEntity entity;
    private String imagePath;

    public PDFService newPDFDocument() {
        this.document = new Document(PageSize.A4);
        return this;
    }

    public PDFService addEntity(AbstractShowableEntity newEntity) {
        this.entity = newEntity;
        return this;
    }

    public PDFService addImage(String imgName) {
        this.imagePath = UPLOAD_PATH + "/preview/" + imgName;
        return this;
    }

    public void build() throws IOException, DocumentException, URISyntaxException {
        PdfWriter.getInstance(document, new FileOutputStream(entity.getName() + ".pdf"));
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);

        document.open();
        document.addLanguage("ru-RU");

        insertImage();
        insertParagraph("\n", font);
        insertParagraph(entity.getCredentials(), font);

        document.close();
    }

    private void insertParagraph(String string, Font font) throws DocumentException {
        Paragraph name = new Paragraph(string, font);
        this.document.add(name);
    }

    private void insertImage() throws IOException, DocumentException {
//        Path path = Paths.get(ClassLoader.getSystemResource(this.imagePath).toURI());
        Path path = Paths.get(this.imagePath);
        Image img = Image.getInstance(path.toAbsolutePath().toString());
        this.document.add(img);
    }
}
