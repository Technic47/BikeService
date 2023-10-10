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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.stream.Stream;

@Component
public class PDFService {
    private String manufacturer;
    private String model;
    private Map<String, String[]> serviceList;
    public static String PDF_DOC_PATH;
    private String fontPath;
    private Font commonFont;
    private Font bigFont;
    public static String PDF_DOC_NAME;
    private String userName;
    public final static Logger logger = LoggerFactory.getLogger("PDFServiceLogger");


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
     * @param item item for list forming.
     */
    public synchronized void build(PdfEntityDto item) {
        // https://coderlessons.com/tutorials/raznoe/uznaite-itext/itext-kratkoe-rukovodstvo
        // https://www.baeldung.com/java-pdf-creation
        Document document = new Document(PageSize.A4);
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
            PdfWriter.getInstance(document, new FileOutputStream(newPDF));
            document.open();

            document.add(insertHeaderTable(item, item.getPicture()));
            if (this.manufacturer != null) {
                document.add(insertManufacture());
            } else document.add(new Paragraph());
            if (this.serviceList != null) {
                document.add(insertServiceTable());
            }

            document.close();
            logger.info("PDF was created for user: " + userName
                    + ". Entity: " + item.getName());
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
            PdfPCell imageCell = new PdfPCell(Image.getInstance(imageBytes));

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
        for (String string : this.serviceList.get("tools")) {
            toolCell.addElement(new Phrase(string, commonFont));
        }
        for (String string : this.serviceList.get("consumables")) {
            consumableCell.addElement(new Phrase(string, commonFont));
        }
        for (String string : this.serviceList.get("fasteners")) {
            consumableCell.addElement(new Phrase(string, commonFont));
        }

        table.addCell(new Phrase("Инструменты", commonFont));
        table.addCell(toolCell);

        table.addCell(new Phrase("Расходные материалы", commonFont));
        table.addCell(consumableCell);

        table.addCell(new Phrase("Крепёж", commonFont));
        table.addCell(fastenerCell);
    }


    private void cleanFields() {
        this.userName = "";
        this.manufacturer = "";
        this.model = "";
        this.serviceList = null;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getModel() {
        return model;
    }

    public Map<String, String[]> getServiceList() {
        return serviceList;
    }

    public String getFontPath() {
        return fontPath;
    }

    public Font getCommonFont() {
        return commonFont;
    }

    public Font getBigFont() {
        return bigFont;
    }

    public String getUserName() {
        return userName;
    }

    @Autowired
    public void setFontPath(@Value("${font.path}") String fontPath) {
        this.fontPath = fontPath;
    }

    @Autowired
    public void setPDFSavePath(@Value("${pdf.path}") String pdfDocPath) {
        PDF_DOC_PATH = pdfDocPath;
    }
}
