package ru.kuznetsov.bikeService.controllers.rest.addition;

import io.nats.client.Connection;
import io.nats.client.Dispatcher;
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
import ru.kuznetsov.bikeService.models.pictures.Picture;
import ru.kuznetsov.bikeService.services.PictureService;
import ru.kuznetsov.bikeService.utils.ByteUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static ru.kuznetsov.bikeService.config.SpringConfig.SUBSCRIBER_Picture;
import static ru.kuznetsov.bikeService.config.SpringConfig.UPLOAD_PATH;

@RestController
@RequestMapping("/api/pictures")
public class PictureControllerREST {
    private final PictureService pictureService;
    private final Connection connection;

    public PictureControllerREST(PictureService pictureService, Connection connection) {
        this.pictureService = pictureService;
        this.connection = connection;
        Dispatcher dispatcher = connection.createDispatcher();
        dispatcher.subscribe(SUBSCRIBER_Picture, msg -> connection.publish(msg.getReplyTo(), getPicture(msg.getData())));
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

    private byte[] getPicture(byte[] bytes) {
        try  {
            Long id = ByteUtils.bytesToLong(bytes);
            Picture picture = pictureService.getById(id);
            Path path = Paths.get(UPLOAD_PATH + "/preview/" + picture.getName());

            return Files.readAllBytes(path);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
