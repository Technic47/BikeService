package ru.kuznetsov.bikeService.controllers.pictures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kuznetsov.bikeService.models.Picture;
import ru.kuznetsov.bikeService.services.PictureService;

@Controller
@RequestMapping("/pictures")
public class PicturesController {
    protected PictureService pictureDao;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("allPictures", pictureDao.index());
        return "pictures/index";
    }

    @GetMapping("/new")
    public String newPicture(Model model) {
        return "pictures/new";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String uploadImage(@RequestPart("newImage") MultipartFile file
    ) {
        PictureWork picWorker = new PictureWork(new Picture());
        picWorker.managePicture(file);
        pictureDao.save(picWorker.getPicture());
        return "redirect:/pictures";
    }

    @PostMapping(value = "/{id}")
    public String delete(@PathVariable("id") Long id) {
        pictureDao.delete(id);
        return "redirect:/pictures";
    }

    @Autowired
    public void setPictureDAO(PictureService pictureDao) {
        this.pictureDao = pictureDao;
    }
}
