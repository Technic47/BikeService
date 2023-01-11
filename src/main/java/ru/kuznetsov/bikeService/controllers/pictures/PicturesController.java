package ru.kuznetsov.bikeService.controllers.pictures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import ru.kuznetsov.bikeService.DAO.DAO;
import ru.kuznetsov.bikeService.models.Picture;

import javax.validation.Valid;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
@RequestMapping("/pictures")
public class PicturesController {
    protected DAO<Picture> pictureDao;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("pictures", pictureDao.index());
        return "pictures/index";
    }

    @GetMapping("/new")
    public String newPicture(Model model) {
        return "pictures/new";
    }

    @PostMapping("/upload")
    public String uploadImage(Model model, @RequestParam("image") MultipartFile file) {
        pictureDao.save(new Picture(file));
//        model.addAttribute("msg", "Uploaded images: " + fileNames.toString());
        return "redirect:/pictures";
    }


    @Autowired
    public void setPictureDAO(DAO<Picture> pictureDao) {
        this.pictureDao = pictureDao;
        this.pictureDao.setCurrentClass(Picture.class);
    }
}
