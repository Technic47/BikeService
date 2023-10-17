package ru.kuznetsov.bikeService.controllers.notRest.additional;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;


@Controller
@RequestMapping("/pictures")
public class PicturesController extends AbstractController {
    @GetMapping
    @Secured("ROLE_ADMIN")
    public String index(Model model, Principal principal) {
        this.addUserToModel(model, principal);
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
    @Secured("ROLE_ADMIN")
    public String delete(@PathVariable("id") Long id) {
        pictureService.delete(id);
        return "redirect:/pictures";
    }
}
