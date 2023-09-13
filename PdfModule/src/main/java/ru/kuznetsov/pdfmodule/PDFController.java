package ru.kuznetsov.pdfmodule;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.SerializationUtils;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static ru.kuznetsov.pdfmodule.PDFService.PDF_DOC_NAME;
import static ru.kuznetsov.pdfmodule.SpringConfig.SUBSCRIBER;


@RestController
//@RequestMapping("/api/pdf")
public class PDFController {
    private final PDFService pdfService;
    private final Connection connection;
    public static final String TMP_PATH = "src/main/resources/tmp/download.dat";


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

    //    @PostMapping()
    public ResponseEntity<Resource> createPdf(PdfEntityDto item) throws IOException {
        pdfService.build(item);
        return this.createResponse(item);
    }

    protected ResponseEntity<Resource> createResponse(PdfEntityDto item) throws IOException {
        File file = new File(PDF_DOC_NAME);
        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource
                (Files.readAllBytes(path));

        return ResponseEntity.ok()
                .headers(this.headers(item.getClass().getSimpleName() + "_" + item.getName()))
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType
                        ("application/octet-stream")).body(resource);
    }

    private HttpHeaders headers(String fileName) {
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + fileName + ".pdf");
        header.add("Cache-Control", "no-cache, no-store,"
                + " must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        return header;
    }
}
