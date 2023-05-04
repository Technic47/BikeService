package ru.kuznetsov.bikeService.controllers.pictures;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kuznetsov.bikeService.controllers.abstracts.AbstractController;

@Controller
@RequestMapping("/pictures")
public class PicturesController extends AbstractController {
    @GetMapping
    public String index(Model model) {
        model.addAttribute("allPictures", pictureService.index());
        return "picture_index";
    }

    @GetMapping("/new")
    public String newPicture() {
        return "picture_new";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String uploadImage(@RequestPart("newImage") MultipartFile file
    ) {
        pictureService.save(file);
        return "redirect:/pictures";
    }

    @PostMapping(value = "/{id}")
    public String delete(@PathVariable("id") Long id) {
        pictureService.delete(id);
        return "redirect:/pictures";
    }
}
