package ru.kuznetsov.pdfmodule;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static ru.kuznetsov.pdfmodule.PDFController.TMP_PATH;

@Component
public class PDFService {
    // https://coderlessons.com/tutorials/raznoe/uznaite-itext/itext-kratkoe-rukovodstvo
    // https://www.baeldung.com/java-pdf-creation
    private Document document;
    private String imagePath;
    private String manufacturer;
    private String model;
    private Set<PdfEntityDto> serviceList;
    public static String PDF_DOC_PATH;
    private String fontPath;
    private Font commonFont;
    private Font bigFont;
    public static String PDF_DOC_NAME;
    private String userName;
    public final static Logger logger = LoggerFactory.getLogger("PDFServiceLogger");

    public PDFService() {
    }

    @PostConstruct
    void setUp() {
        try {
            this.setFonts(this.fontPath);
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn(e.getMessage());
        }
    }

    public void setFonts(String string) throws DocumentException, IOException {
        BaseFont baseFont = BaseFont.createFont(string, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        this.commonFont = new Font(baseFont, 10, Font.NORMAL);

        BaseFont baseFont2 = BaseFont.createFont(string, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        this.bigFont = new Font(baseFont2, 16, Font.BOLD);
    }


    /**
     * Construct PDF document.
     *
     * @param item     item for list forming.
     */
    public void build(PdfEntityDto item, byte[] imageBytes) {
        this.document = new Document(PageSize.A4);
        this.userName = item.getUserName();
        this.manufacturer = item.getManufacturer();
        this.model = item.getModel();
        this.serviceList = item.getLinkedItems();

        try {
            PDF_DOC_NAME = PDF_DOC_PATH + this.userName + ".pdf";
            File newPDF = new File(PDF_DOC_NAME);
            if (!newPDF.exists()) {
                newPDF.createNewFile();
            }
            PdfWriter.getInstance(this.document, new FileOutputStream(newPDF));
            document.open();

            document.add(insertHeaderTable(item, imageBytes));
            if (this.manufacturer != null) {
                document.add(insertManufacture());
            } else document.add(new Paragraph());
            if (this.serviceList != null) {
                document.add(insertServiceTable());
            }

            document.close();
//            this.clean(PDF_DOC_NAME);
            this.cleanFields();
        } catch (Exception e) {
            e.printStackTrace();
            logger.warn(e.getMessage());
        }
    }

    private Element insertManufacture() {
        String modelInfo = this.manufacturer + " - " + this.model;
        return insertParagraph(modelInfo, commonFont);
    }

    private Paragraph insertParagraph(String string, Font font) {
        Paragraph paragraph = new Paragraph(string, font);
        paragraph.setAlignment(Element.ALIGN_CENTER);
        return paragraph;
    }

    //Table former for header of document
    private PdfPTable insertHeaderTable(PdfEntityDto item, byte[] imageBytes) {
        PdfPTable table = new PdfPTable(2);
        try {
            Path path = Paths.get(TMP_PATH);
            PdfPCell imageCell = new PdfPCell(Image.getInstance(imageBytes));
//            PdfPCell imageCell = new PdfPCell(Image.getInstance(path.toAbsolutePath().toString()));
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
            logger.warn(e.getMessage());
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
        PdfPCell toolCell = new PdfPCell();
        PdfPCell consumableCell = new PdfPCell();
        PdfPCell fastenerCell = new PdfPCell();

        this.serviceList.forEach(item -> {
            switch (item.getCategory()) {
                case "Tool" -> toolCell.addElement(new Phrase(item.getName(), commonFont));
                case "Consumable" ->
                        consumableCell.addElement(new Phrase(item.getName() + ", " + item.getAmount(), commonFont));
                case "Fastener" ->
                        fastenerCell.addElement(new Phrase(item.getName() + ", " + item.getAmount(), commonFont));
            }
        });

        table.addCell(new Phrase("Инструменты", commonFont));
        table.addCell(toolCell);

        table.addCell(new Phrase("Расходные материалы", commonFont));
        table.addCell(consumableCell);

        table.addCell(new Phrase("Крепёж", commonFont));
        table.addCell(fastenerCell);
    }

    public boolean cleanFile(String path) {
        return new File(path).delete();
    }

    private void cleanFields() {
        this.userName = "";
        this.manufacturer = "";
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

    public String getManufacturer() {
        return manufacturer;
    }

    public Set<PdfEntityDto> getServiceList() {
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
