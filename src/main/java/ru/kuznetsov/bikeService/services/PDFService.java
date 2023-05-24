package ru.kuznetsov.bikeService.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Component;
import ru.kuznetsov.bikeService.controllers.abstracts.AbstractController;
import ru.kuznetsov.bikeService.models.abstracts.AbstractServiceableEntity;
import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;
import ru.kuznetsov.bikeService.models.abstracts.AbstractUsableEntity;
import ru.kuznetsov.bikeService.models.lists.ServiceList;
import ru.kuznetsov.bikeService.models.showable.Manufacturer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static ru.kuznetsov.bikeService.config.SpringConfig.UPLOAD_PATH;


@Component
public class PDFService {
    // https://coderlessons.com/tutorials/raznoe/uznaite-itext/itext-kratkoe-rukovodstvo
    // https://www.baeldung.com/java-pdf-creation
    private Document document;
    private String imagePath;
    private Manufacturer manufacturer;
    private ServiceList serviceList;
    private Font commonFont;
    private Font bigFont;

    public PDFService() {
        try {
            BaseFont baseFont = BaseFont.createFont("fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            this.commonFont = new Font(baseFont, 10, Font.NORMAL);

            BaseFont baseFont2 = BaseFont.createFont("fonts/arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            this.bigFont = new Font(baseFont2, 16, Font.BOLD);
        } catch (Exception e) {
            e.printStackTrace();
            AbstractController.logger.warn(e.getMessage());
        }
    }

    public PDFService newPDFDocument() {
        this.document = new Document(PageSize.A4);
        return this;
    }

    public PDFService addImage(String imgName) {
        this.imagePath = UPLOAD_PATH + "/preview/" + imgName;
        return this;
    }

    public PDFService addManufactorer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
        return this;
    }

    public PDFService addServiceList(ServiceList list) {
        this.serviceList = list;
        return this;
    }

    public <T extends AbstractShowableEntity> void build(T item) {
        try {
            PdfWriter.getInstance(this.document, new FileOutputStream(item.getName() + ".pdf"));
            document.open();

            insertImage();
            insertParagraph("\n", commonFont);
            insertParagraph(item.getCredentials(), bigFont);
            insertParagraph(item.getDescription(), commonFont);

            switch (item.getClass().getSimpleName()) {
                case "Tool", "Consumable" -> this.buildUsable((AbstractUsableEntity) item);
                case "Bike", "Part" -> this.buildServiceable((AbstractServiceableEntity) item);
            }

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
            AbstractController.logger.warn(e.getMessage());
        }
    }

    private void buildUsable(AbstractUsableEntity newEntity) throws DocumentException {
        String modelInfo = this.manufacturer.getName() + " - " + newEntity.getModel();
        insertParagraph(modelInfo, commonFont);
    }

    private void buildServiceable(AbstractServiceableEntity newEntity) throws DocumentException {
        this.buildUsable(newEntity);
        insertParagraph("\n", commonFont);
        insertTable();
    }

    private void insertParagraph(String string, Font font) throws DocumentException {
        Paragraph part = new Paragraph(string, font);
        this.document.add(part);
    }

    private void insertImage() throws IOException, DocumentException {
        Path path = Paths.get(this.imagePath);
        Image img = Image.getInstance(path.toAbsolutePath().toString());
        this.document.add(img);
    }

    private void insertTable() throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        addTableHeader(table);
        addSelfRows(table);
        document.add(table);
    }

    private void addTableHeader(PdfPTable table) {
        Stream.of("Тип", "Список")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.WHITE);
//                    header.setBorderWidth(1);
                    header.setPhrase(new Phrase(columnTitle, bigFont));
                    table.addCell(header);
                });
    }

    private void addSelfRows(PdfPTable table) {
        table.addCell(new Phrase("Инструменты", commonFont));
        PdfPCell toolCell = new PdfPCell();
        this.serviceList.getToolMap().forEach((key, value) -> {
            toolCell.addElement(new Phrase(key.getCredentials(), commonFont));
        });
        table.addCell(toolCell);

        table.addCell(new Phrase("Расходные материалы", commonFont));
        PdfPCell consumableCell = new PdfPCell();
        this.serviceList.getConsumableMap().forEach((key, value) -> {
            consumableCell.addElement(new Phrase(key.getName() + ", " + value.toString(), commonFont));
        });
        table.addCell(consumableCell);

        table.addCell(new Phrase("Крепёж", commonFont));
        PdfPCell fastenerCell = new PdfPCell();
        this.serviceList.getFastenerMap().forEach((key, value) -> {
            fastenerCell.addElement(new Phrase(key.getName() + ", " + value.toString(), commonFont));
        });
        table.addCell(fastenerCell);
    }
}
