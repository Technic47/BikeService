package ru.kuznetsov.bikeService.services;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.kuznetsov.bikeService.models.lists.ServiceList;
import ru.kuznetsov.bikeService.models.showable.Fastener;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static ru.kuznetsov.bikeService.TestCredentials.*;
import static ru.kuznetsov.bikeService.services.PDFService.PDF_DOC_NAME;

@SpringBootTest
@TestPropertySource("/application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PDFServiceTest {
    @Autowired
    private PDFService service;


    @Test
    @Order(1)
    void creationTest() {
        assertNotNull(service.getFontPath());
        assertNotNull(service.getCommonFont());
        assertNotNull(service.getBigFont());
    }

    @Test
    @Order(2)
    void newPDFDocument() {
        this.service.newPDFDocument();

        assertNotNull(service.getDocument());
    }

    @Test
    @Order(3)
    void addImage() {
        service.addImage(TEST_NAME);

        assertNotNull(service.getImagePath());
    }

    @Test
    @Order(4)
    void addManufactorer() {
        service.addManufacturer(TEST_MANUFACTURER);

        assertEquals(TEST_MANUFACTURER, service.getManufacturer());
    }

    @Test
    @Order(5)
    void addServiceList() {
        service.addServiceList(new ServiceList());

        assertNotNull(service.getServiceList());
    }

    @Test
    @Order(8)
    void buildShowable() throws IOException {
        service.newPDFDocument()
                .addUserName(TEST_NAME)
                .addImage("testImage.jpg")
                .buildShowable(TEST_FASTENER);

        File formedFile = new File(PDF_DOC_NAME);

        PdfReader reader = new PdfReader(PDF_DOC_NAME);
        int pages = reader.getNumberOfPages();
        StringBuilder text = new StringBuilder();
        for (int i = 1; i <= pages; i++) {
            text.append(PdfTextExtractor.getTextFromPage(reader, i));
        }
        reader.close();

        assertTrue(formedFile.exists());
        assertTrue(text.toString().contains(TEST_NAME));
        assertTrue(text.toString().contains(TEST_DESCRIPTION));

        this.service.cleanFile(PDF_DOC_NAME);
    }

    @Test
    @Order(7)
    void buildUsable() throws IOException {
        service.newPDFDocument()
                .addUserName(TEST_NAME)
                .addImage("testImage.jpg")
                .addManufacturer(TEST_MANUFACTURER)
                .buildShowable(TEST_TOOL);

        File formedFile = new File(PDF_DOC_NAME);

        PdfReader reader = new PdfReader(PDF_DOC_NAME);
        int pages = reader.getNumberOfPages();
        StringBuilder text = new StringBuilder();
        for (int i = 1; i <= pages; i++) {
            text.append(PdfTextExtractor.getTextFromPage(reader, i));
        }
        reader.close();

        assertTrue(formedFile.exists());
        assertTrue(text.toString().contains(TEST_NAME));
        assertTrue(text.toString().contains(TEST_DESCRIPTION));
        assertTrue(text.toString().contains(TEST_MANUFACTURER.getName()));
        assertTrue(text.toString().contains(TEST_MODEL));

        this.service.cleanFile(PDF_DOC_NAME);
    }

    @Test
    @Order(6)
    void buildServiceable() throws IOException {
        ServiceList list = new ServiceList();
        list.addToConsumableMap(TEST_CONSUMABLE, 1);
        list.addToToolMap(TEST_TOOL, 1);
        Fastener fastener = new Fastener();
        fastener.setName("qwerty");
        list.addToFastenerMap(fastener, 123);

        service.newPDFDocument()
                .addUserName(TEST_NAME)
                .addImage("testImage.jpg")
                .addManufacturer(TEST_MANUFACTURER)
                .addServiceList(list)
                .buildShowable(TEST_PART);

        File formedFile = new File(PDF_DOC_NAME);

        PdfReader reader = new PdfReader(PDF_DOC_NAME);
        int pages = reader.getNumberOfPages();
        StringBuilder text = new StringBuilder();
        for (int i = 1; i <= pages; i++) {
            text.append(PdfTextExtractor.getTextFromPage(reader, i));
        }
        reader.close();

        assertTrue(formedFile.exists());
        assertTrue(text.toString().contains(TEST_NAME));
        assertTrue(text.toString().contains(TEST_DESCRIPTION));
        assertTrue(text.toString().contains(TEST_MANUFACTURER.getName()));
        assertTrue(text.toString().contains(TEST_MODEL));
        assertTrue(text.toString().contains(TEST_VALUE));
        assertTrue(text.toString().contains("qwerty"));
        assertTrue(text.toString().contains("123"));

        this.service.cleanFile(PDF_DOC_NAME);
    }


    @Test
    @Order(9)
    void cleanTest() throws IOException {
        this.buildShowable();
        assertTrue(new File(PDF_DOC_NAME).exists());

        assertFalse(service.cleanFile(PDF_DOC_NAME));
    }

    @Test
    @Order(10)
    void wrongFontSet() {
        assertThrows(DocumentException.class, () -> {
            service.setFonts(TEST_NAME);
        });
    }
}