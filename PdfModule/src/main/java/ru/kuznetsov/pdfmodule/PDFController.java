package ru.kuznetsov.pdfmodule;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static ru.kuznetsov.pdfmodule.PDFService.PDF_DOC_NAME;
import static ru.kuznetsov.pdfmodule.SpringConfig.SUBSCRIBER;


@RestController
//@RequestMapping("/api/pdf")
public class PDFController {
    private final PDFService pdfService;
    private final Connection connection;
    public static final String TMP_PATH = "src/main/resources/tmp/download.jpg";


    public PDFController(PDFService pdfService, Connection connection) {
        this.pdfService = pdfService;
        this.connection = connection;
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
