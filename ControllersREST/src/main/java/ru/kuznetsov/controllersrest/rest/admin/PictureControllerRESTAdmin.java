package ru.kuznetsov.controllersrest.rest.admin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bikeservice.mainresources.controllers.abstracts.AbstractEntityController;
import ru.bikeservice.mainresources.models.pictures.Picture;

import java.util.List;

@RestController
@RequestMapping("/api/admin/pictures")
public class PictureControllerRESTAdmin extends AbstractEntityController {
    @GetMapping
    public List<Picture> index() {
        return pictureService.getAll();
    }

    @GetMapping("/{id}")
    public Picture getUser(@PathVariable Long id) {
        return pictureService.getById(id);
    }
}