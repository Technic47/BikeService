package ru.kuznetsov.bikeService.services;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
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
    private ExecutorService mainExecutor;

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

    public PDFService addManufacturer(Manufacturer manufacturer) {
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
    public <T extends AbstractShowableEntity> void buildShowable(T item) {
        try {
            PDF_DOC_NAME = PDF_DOC_PATH + this.userName + ".pdf";
            PdfWriter.getInstance(this.document, new FileOutputStream(PDF_DOC_NAME));
            document.open();
            document.add(insertHeaderTable(item));
            document.close();
//            this.clean(PDF_DOC_NAME);
            this.cleanFields();
        } catch (Exception e) {
            e.printStackTrace();
            AbstractController.logger.warn(e.getMessage());
        }
    }

    public <T extends AbstractUsableEntity> void buildUsable(T item) {
        try {
            PDF_DOC_NAME = PDF_DOC_PATH + this.userName + ".pdf";
            PdfWriter.getInstance(this.document, new FileOutputStream(PDF_DOC_NAME));
            document.open();
            Future<Element> headerTable = mainExecutor.submit(() -> insertHeaderTable(item));
            Future<Element> manufacture = mainExecutor.submit(() -> insertManufacture(item));
            document.add(headerTable.get());
            document.add(manufacture.get());
            document.close();
//            this.clean(PDF_DOC_NAME);
            this.cleanFields();
        } catch (Exception e) {
            e.printStackTrace();
            AbstractController.logger.warn(e.getMessage());
        }
    }

    public <T extends AbstractServiceableEntity> void buildServiceable(T item) {
        try {
            PDF_DOC_NAME = PDF_DOC_PATH + this.userName + ".pdf";
            PdfWriter.getInstance(this.document, new FileOutputStream(PDF_DOC_NAME));
            document.open();

            Future<Element> headerTable = mainExecutor.submit(() -> insertHeaderTable(item));
            Future<Element> manufacture = mainExecutor.submit(() -> insertManufacture(item));
            Future<Element> serviceTable = mainExecutor.submit(this::insertServiceTable);

            document.add(headerTable.get());
            document.add(manufacture.get());
            document.add(serviceTable.get());

            document.close();
//            this.clean(PDF_DOC_NAME);
            this.cleanFields();
        } catch (Exception e) {
            e.printStackTrace();
            AbstractController.logger.warn(e.getMessage());
        }
    }

    private Element insertManufacture(AbstractUsableEntity newEntity) {
        if (this.manufacturer != null) {
            String modelInfo = this.manufacturer.getName() + " - " + newEntity.getModel();
            return insertParagraph(modelInfo, commonFont);
        } else return new Paragraph();
    }

    private Paragraph insertParagraph(String string, Font font) {
        Paragraph paragraph = new Paragraph(string, font);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        return paragraph;
    }

    //Table former for header of document
    private <T extends AbstractShowableEntity> PdfPTable insertHeaderTable(T item) {
        PdfPTable table = new PdfPTable(2);
        try {
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
            return table;
        } catch (Exception e) {
            e.printStackTrace();
            AbstractController.logger.warn(e.getMessage());
        }
        return table;
    }

    //Table former for serviceList
    private PdfPTable insertServiceTable() {
        PdfPTable table = new PdfPTable(2);
        addTableHeader(table);
        addSelfRows(table);
        return table;
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
        this.serviceList.getToolMap().forEach((key, value) -> toolCell.addElement(new Phrase(key.getCredentials(), commonFont)));
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

    public boolean cleanFile(String path) {
        return new File(path).delete();
    }

    private void cleanFields() {
        this.userName = "";
        this.manufacturer = null;
        this.imagePath = "";
        this.serviceList = null;
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

    @Autowired
    @Qualifier("MainExecutor")
    public void setMainExecutor(ExecutorService mainExecutor) {
        this.mainExecutor = mainExecutor;
    }
}
