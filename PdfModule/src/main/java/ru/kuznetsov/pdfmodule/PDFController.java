package ru.kuznetsov.pdfmodule;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static ru.kuznetsov.pdfmodule.PDFService.PDF_DOC_NAME;


@RestController
@RequestMapping("/api/pdf")
public class PDFController {
    private final PDFService pdfService;
    private final WebClient webClient;
    public static final String TMP_PATH = "src/main/resources/tmp/download.dat";

    public PDFController(PDFService pdfService, WebClient webClient) {
        this.pdfService = pdfService;
        this.webClient = webClient;
    }


    //    @Operation(summary = "Build PDF document")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "PDF created",
//                    content = @Content),
//            @ApiResponse(responseCode = "403", description = "Access denied",
//                    content = @Content),
//            @ApiResponse(responseCode = "404", description = "Entity not found",
//                    content = @Content)})
    @GetMapping()
//    @ResponseBody
    public ResponseEntity<Resource> createPdf(@RequestBody PdfEntityDto item) throws IOException {
        byte[] imageBytes = webClient.get()
                .uri(String.join("", "/api/pictures/" + item.getPicture()))
                .accept(MediaType.ALL)
                .retrieve()
                .bodyToMono(byte[].class)
//                .doOnError(error -> System.out.println(error.getMessage()))
//                .doOnSuccess(System.out::println)
//                .retryWhen(Retry.fixedDelay(3, Duration.ofMillis(100)))
                .block();
        Path path = Paths.get(TMP_PATH);
        Files.write(path, imageBytes);

        pdfService.build(item);
        return this.createResponse(item);
    }

    protected ResponseEntity<Resource> createResponse(PdfEntityDto item) throws IOException {
        File file = new File(PDF_DOC_NAME);
        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource
                (Files.readAllBytes(path));

        return ResponseEntity.ok().headers(this.headers(item.getClass().getSimpleName() + "_" + item.getName()))
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
