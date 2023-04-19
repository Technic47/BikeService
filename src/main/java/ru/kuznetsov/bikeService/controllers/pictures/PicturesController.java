package ru.kuznetsov.bikeService.controllers.pictures;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kuznetsov.bikeService.controllers.abstracts.AbstractController;
import ru.kuznetsov.bikeService.models.pictures.Picture;
import ru.kuznetsov.bikeService.models.pictures.PictureWork;

@Controller
@RequestMapping("/pictures")
public class PicturesController extends AbstractController {
    @GetMapping
    public String index(Model model) {
        model.addAttribute("allPictures", pictureService.index());
        return "pictures/index";
    }

    @GetMapping("/new")
    public String newPicture() {
        return "pictures/new";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String uploadImage(@RequestPart("newImage") MultipartFile file
    ) {
        PictureWork picWorker = new PictureWork(new Picture());
        picWorker.managePicture(file);
        pictureService.save(picWorker.getPicture());
        return "redirect:/pictures";
    }

    @PostMapping(value = "/{id}")
    public String delete(@PathVariable("id") Long id) {
        pictureService.delete(id);
        return "redirect:/pictures";
    }
}
