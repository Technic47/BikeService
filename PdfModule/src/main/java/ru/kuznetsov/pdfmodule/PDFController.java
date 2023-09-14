package ru.kuznetsov.pdfmodule;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static ru.kuznetsov.pdfmodule.PDFService.PDF_DOC_NAME;
import static ru.kuznetsov.pdfmodule.SpringConfig.SUBSCRIBER;

@Controller
public class PDFController {
    private final PDFService pdfService;

    public PDFController(PDFService pdfService, Connection connection) {
        this.pdfService = pdfService;
        Dispatcher dispatcher = connection.createDispatcher();
        dispatcher.subscribe(SUBSCRIBER, msg -> connection.publish(msg.getReplyTo(), getData(msg.getData())));
    }

    public byte[] getData(byte[] bytes) {
        PdfEntityDto item = new PdfEntityDto(bytes);
        try {
            pdfService.build(item);
            File file = new File(PDF_DOC_NAME);
            Path path = Paths.get(file.getAbsolutePath());

            return Files.readAllBytes(path);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
