package ru.kuznetsov.bikeService.services;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PDFServiceTest {

    private final PDFService service = new PDFService();

    @Test
    void creationTest(){
        assertThat(service.getCommonFont()).isNotNull();
        assertThat(service.getBigFont()).isNotNull();
    }

    @Test
    void newPDFDocument() {
    }

    @Test
    void addImage() {
    }

    @Test
    void addManufactorer() {
    }

    @Test
    void addServiceList() {
    }

    @Test
    void build() {
    }
}