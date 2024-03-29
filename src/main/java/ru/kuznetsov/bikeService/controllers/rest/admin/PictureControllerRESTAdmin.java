package ru.kuznetsov.bikeService.controllers.rest.admin;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kuznetsov.bikeService.controllers.abstracts.CommonEntityController;
import ru.kuznetsov.bikeService.models.pictures.Picture;

import java.util.List;

@RestController
@RequestMapping("/api/admin/pictures")
public class PictureControllerRESTAdmin extends CommonEntityController {
    @GetMapping
    public List<Picture> index() {
        return pictureService.index();
    }

    @GetMapping("/{id}")
    public Picture getUser(@PathVariable Long id) {
        return pictureService.getById(id);
    }
}
