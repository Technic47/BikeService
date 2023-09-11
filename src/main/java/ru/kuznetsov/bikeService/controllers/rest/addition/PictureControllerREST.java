package ru.kuznetsov.bikeService.controllers.rest.addition;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kuznetsov.bikeService.models.pictures.Picture;
import ru.kuznetsov.bikeService.services.PictureService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/pictures")
public class PictureControllerREST {
    private final PictureService pictureService;
    @Value("${upload.path}")
    private String uploadPath;

    public PictureControllerREST(PictureService pictureService) {
        this.pictureService = pictureService;
    }

//    @Operation(summary = "Get picture bytes")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Success",
//                    content = {@Content(mediaType = "IMAGE_JPEG_VALUE",
//                            schema = @Schema(implementation = byte[].class))}),
//            @ApiResponse(responseCode = "404", description = "Entity not found",
//                    content = @Content)})
//    @GetMapping(value = "/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
//    public @ResponseBody byte[] getImage(@PathVariable Long id) throws IOException {
//        Picture picture = pictureService.getById(id);
//        String path = "/IMG/" + picture.getName();
//        InputStream in = getClass()
//                .getResourceAsStream(path);
//        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
//        int nRead;
//        byte[] data = new byte[4];
//
//        while ((nRead = in.readNBytes(data, 0, data.length)) != 0) {
//            buffer.write(data, 0, nRead);
//        }
//
//        buffer.flush();
//        return buffer.toByteArray();
//    }

    @GetMapping(value = "/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Resource> get(@PathVariable Long id) {
        System.out.println("Method called with id: " + id);
        Picture picture = pictureService.getById(id);
        String path = uploadPath + "/" + picture.getName();
        return ResponseEntity.ok()
                .body(new FileSystemResource(Paths.get(path)));
    }
}
