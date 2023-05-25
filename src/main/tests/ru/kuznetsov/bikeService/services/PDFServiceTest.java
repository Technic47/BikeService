package ru.kuznetsov.bikeService.services;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.kuznetsov.bikeService.models.lists.ServiceList;
import ru.kuznetsov.bikeService.models.showable.Fastener;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static ru.kuznetsov.bikeService.TestCredentials.*;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class PDFServiceTest {
    @Autowired
    private PDFService service;


    @Test
    void creationTest() {
        assertNotNull(service.getPath());
        assertNotNull(service.getCommonFont());
        assertNotNull(service.getBigFont());
    }

    @Test
    void wrongFontSet() {
        assertThrows(Exception.class, () -> {
            service.setFonts(TEST_NAME);
        });
    }

    @Test
    void newPDFDocument() {
        this.service.newPDFDocument();

        assertNotNull(service.getDocument());
    }

    @Test
    void addImage() {
        service.addImage(TEST_NAME);

        assertNotNull(service.getImagePath());
    }

    @Test
    void addManufactorer() {
        service.addManufactorer(TEST_MANUFACTURER);

        assertEquals(TEST_MANUFACTURER, service.getManufacturer());
    }

    @Test
    void addServiceList() {
        service.addServiceList(new ServiceList());

        assertNotNull(service.getServiceList());
    }

    @Test
    void buildShowable() throws IOException {
        service.newPDFDocument()
                .addImage("testImage.jpg")
                .build(TEST_FASTENER);

        File formedFile = new File("FormedList.pdf");

        PdfReader reader = new PdfReader("FormedList.pdf");
        int pages = reader.getNumberOfPages();
        StringBuilder text = new StringBuilder();
        for (int i = 1; i <= pages; i++) {
            text.append(PdfTextExtractor.getTextFromPage(reader, i));
        }
        reader.close();

        assertTrue(formedFile.exists());
        assertTrue(text.toString().contains(TEST_NAME));
        assertTrue(text.toString().contains(TEST_DESCRIPTION));
    }

    @Test
    void buildUsable() throws IOException {
        service.newPDFDocument()
                .addImage("testImage.jpg")
                .addManufactorer(TEST_MANUFACTURER)
                .build(TEST_TOOL);

        File formedFile = new File("FormedList.pdf");

        PdfReader reader = new PdfReader("FormedList.pdf");
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
    }

    @Test
    void buildServiceable() throws IOException {
        ServiceList list = new ServiceList();
        list.addToConsumableMap(TEST_CONSUMABLE, 1);
        list.addToToolMap(TEST_TOOL, 1);
        Fastener fastener = new Fastener();
        fastener.setName("qwerty");
        list.addToFastenerMap(fastener, 123);

        service.newPDFDocument()
                .addImage("testImage.jpg")
                .addManufactorer(TEST_MANUFACTURER)
                .addServiceList(list)
                .build(TEST_PART);

        File formedFile = new File("FormedList.pdf");

        PdfReader reader = new PdfReader("FormedList.pdf");
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
    }
}