package ru.kuznetsov.bikeService.controllers.rest.addition;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bikeservice.mainresources.models.pictures.Picture;
import ru.bikeservice.mainresources.services.PictureService;

import java.nio.file.Paths;

import static ru.kuznetsov.bikeService.config.SpringConfig.UPLOAD_PATH;

@RestController
@RequestMapping("/api/pictures")
public class PictureControllerREST {
    private final PictureService pictureService;

    public PictureControllerREST(PictureService pictureService) {
        this.pictureService = pictureService;
    }

    @Operation(summary = "Get picture")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success",
                    content = {@Content(mediaType = "IMAGE_JPEG_VALUE",
                            schema = @Schema(implementation = byte[].class))}),
            @ApiResponse(responseCode = "404", description = "Entity not found",
                    content = @Content)})
    @GetMapping(value = "/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<Resource> get(@PathVariable Long id) {
        Picture picture = pictureService.getById(id);
        String path = UPLOAD_PATH + "/preview/" + picture.getName();
        return ResponseEntity.ok()
                .body(new FileSystemResource(Paths.get(path)));
    }
}
