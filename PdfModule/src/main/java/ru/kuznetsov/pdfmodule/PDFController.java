package ru.kuznetsov.pdfmodule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
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

//    @Value("${kafka.group.id}")
//    private String kafkaGroupId;
//    @Value("${kafka.request.topic}")
//    private String topic;

//    @Autowired
//    private KafkaTemplate<String, byte[]> kafkaTemplate;

    @Autowired
    public PDFController(PDFService pdfService
//            , Connection connection
    ) {
        this.pdfService = pdfService;
//        Dispatcher dispatcher = connection.createDispatcher();
//        dispatcher.subscribe(SUBSCRIBER, msg -> connection.publish(msg.getReplyTo(), getData(msg.getData())));
    }

    @KafkaListener(topics = "pdf", id = "${kafka.group.id}")
    @SendTo
    public byte[] listenGroupFoo(PdfEntityDto message) {
//        System.out.println("Received Message in group foo: " + message);
//        byte[] bytes = Base64.getDecoder().decode(message);
//        String reply = Base64.getEncoder().encodeToString(getData(bytes));
        return getData(message);
//        kafkaTemplate.send("pdf", getData(message));

    }

    public byte[] getData(PdfEntityDto bytes) {
//        PdfEntityDto item = new PdfEntityDto(bytes);
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
