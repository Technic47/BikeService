package ru.kuznetsov.pdfmodule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static ru.kuznetsov.pdfmodule.PDFService.PDF_DOC_NAME;

@Component
public class PDFController {
    private final PDFService pdfService;

    @Autowired
    public PDFController(PDFService pdfService
    ) {
        this.pdfService = pdfService;
    }

    @KafkaListener(topics = "pdf", id = "pdfBuilder")
    @SendTo
    public byte[] listenGroupFoo(PdfEntityDto message) {
        return getData(message);
    }

    public byte[] getData(PdfEntityDto bytes) {
        try {
            pdfService.build(bytes);
            File file = new File(PDF_DOC_NAME);
            Path path = Paths.get(file.getAbsolutePath());

            return Files.readAllBytes(path);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
