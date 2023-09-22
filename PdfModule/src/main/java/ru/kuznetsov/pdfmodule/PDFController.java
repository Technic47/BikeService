package ru.kuznetsov.pdfmodule;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

import static ru.kuznetsov.pdfmodule.PDFService.PDF_DOC_NAME;

@Controller
public class PDFController {
    private final PDFService pdfService;

    @Value("${kafka.group.id}")
    private String kafkaGroupId;

    @Autowired
    public PDFController(PDFService pdfService
//            , Connection connection
    ) {
        this.pdfService = pdfService;
//        Dispatcher dispatcher = connection.createDispatcher();
//        dispatcher.subscribe(SUBSCRIBER, msg -> connection.publish(msg.getReplyTo(), getData(msg.getData())));
    }

    @KafkaListener(topics = "pdf", id = "pdfBuilder", containerFactory = "kafkaListenerContainerFactory")
    public void listenGroupFoo(String message) {
//        Base64.getDecoder().decode(message);
        System.out.println("Received Message in group foo: " + message.length());
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
