package ru.kuznetsov.bikeService.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.kuznetsov.bikeService.controllers.abstracts.AbstractController;
import ru.kuznetsov.bikeService.models.abstracts.AbstractServiceableEntity;
import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;
import ru.kuznetsov.bikeService.models.abstracts.AbstractUsableEntity;
import ru.kuznetsov.bikeService.models.lists.ServiceList;
import ru.kuznetsov.bikeService.models.showable.Manufacturer;

import java.io.File;
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
    public static String PDF_DOC_PATH;
    private String fontPath;
    private Font commonFont;
    private Font bigFont;
    public static String PDF_DOC_NAME;
    private String userName;

    public PDFService() {
    }

    @PostConstruct
    void setUp() {
        try {
            this.setFonts(this.fontPath);
        } catch (Exception e) {
            e.printStackTrace();
            AbstractController.logger.warn(e.getMessage());
        }
    }

    public void setFonts(String string) throws DocumentException, IOException {
        BaseFont baseFont = BaseFont.createFont(string, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        this.commonFont = new Font(baseFont, 10, Font.NORMAL);

        BaseFont baseFont2 = BaseFont.createFont(string, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        this.bigFont = new Font(baseFont2, 16, Font.BOLD);
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

    public PDFService addUserName(String name) {
        this.userName = name;
        return this;
    }

    /**
     * Construct PDF document. Construction is based on T item class.
     * Usable adds Manufacture info.
     * Serviceable adds info about linkedItems.
     *
     * @param item item for list forming.
     * @param <T>  class from .model package.
     */
    public <T extends AbstractShowableEntity> void build(T item) {
        try {
            PDF_DOC_NAME = PDF_DOC_PATH + this.userName + ".pdf";
            PdfWriter.getInstance(this.document, new FileOutputStream(PDF_DOC_NAME));
            document.open();
            insertHeaderTable(item);

            switch (item.getClass().getSimpleName()) {
                case "Tool", "Consumable" -> this.buildUsable((AbstractUsableEntity) item);
                case "Bike", "Part" -> this.buildServiceable((AbstractServiceableEntity) item);
            }
            document.close();
//            this.clean(PDF_DOC_NAME);
        } catch (Exception e) {
            e.printStackTrace();
            AbstractController.logger.warn(e.getMessage());
        }
    }

    private void buildUsable(AbstractUsableEntity newEntity) throws DocumentException {
        if (this.manufacturer != null) {
            String modelInfo = this.manufacturer.getName() + " - " + newEntity.getModel();
            insertParagraph(modelInfo, commonFont);
        }
    }

    private void buildServiceable(AbstractServiceableEntity newEntity) throws DocumentException {
        this.buildUsable(newEntity);
        insertParagraph("\n", commonFont);
        if (this.serviceList != null) {
            insertServiceTable();
        }
    }

    private void insertParagraph(String string, Font font) throws DocumentException {
        Paragraph part = new Paragraph(string, font);
        part.setAlignment(Element.ALIGN_CENTER);
        this.document.add(part);
    }

    //Table former for header of document
    private <T extends AbstractShowableEntity> void insertHeaderTable(T item) {
        try {
            PdfPTable table = new PdfPTable(2);

            Path path = Paths.get(this.imagePath);
            PdfPCell imageCell = new PdfPCell(Image.getInstance(path.toAbsolutePath().toString()));
            imageCell.setBorder(Rectangle.NO_BORDER);

            PdfPCell content = new PdfPCell();
            content.setBorder(Rectangle.NO_BORDER);
            content.setHorizontalAlignment(Element.ALIGN_CENTER);
            content.addElement(new Paragraph(item.getName(), bigFont));
            content.addElement(new Paragraph(item.getValueName()
                    + " " + item.getValue(), commonFont));
            content.addElement(new Paragraph(item.getDescription(), commonFont));

            table.addCell(imageCell);
            table.addCell(content);
            document.add(table);
        } catch (Exception e) {
            e.printStackTrace();
            AbstractController.logger.warn(e.getMessage());
        }
    }

    //Table former for serviceList
    private void insertServiceTable() {
        try {
            PdfPTable table = new PdfPTable(2);
            addTableHeader(table);
            addSelfRows(table);
            document.add(table);
        } catch (Exception e) {
            e.printStackTrace();
            AbstractController.logger.warn(e.getMessage());
        }
    }

    private void addTableHeader(PdfPTable table) {
        Stream.of("Тип", "Список")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.WHITE);
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
            consumableCell.addElement(new Phrase(key.getName()
                    + ", " + value.toString(), commonFont));
        });
        table.addCell(consumableCell);

        table.addCell(new Phrase("Крепёж", commonFont));
        PdfPCell fastenerCell = new PdfPCell();
        this.serviceList.getFastenerMap().forEach((key, value) -> {
            fastenerCell.addElement(new Phrase(key.getName()
                    + ", " + value.toString(), commonFont));
        });
        table.addCell(fastenerCell);
    }

    public boolean clean(String path) {
        return new File(path).delete();
    }

    public String getFontPath() {
        return fontPath;
    }

    public Document getDocument() {
        return document;
    }

    public String getImagePath() {
        return imagePath;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public ServiceList getServiceList() {
        return serviceList;
    }

    @Autowired
    public void setFontPath(@Value("${font.path}") String fontPath) {
        this.fontPath = fontPath;
    }

    @Autowired
    public void setPDFSavePath(@Value("${pdf.path}") String pdfDocPath) {
        PDF_DOC_PATH = pdfDocPath;
    }

    public Font getCommonFont() {
        return commonFont;
    }

    public Font getBigFont() {
        return bigFont;
    }
}
