package ru.kuznetsov.bikeService.controllers.pictures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kuznetsov.bikeService.DAO.DAO;
import ru.kuznetsov.bikeService.models.Picture;

@Controller
@RequestMapping("/pictures")
public class PicturesController {
    protected DAO<Picture> pictureDao;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("allPictures", pictureDao.index());
        return "pictures/index";
    }

    @GetMapping("/new")
    public String newPicture(Model model) {
        model.addAttribute("message", "Hello!");
        return "pictures/new";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public String uploadImage(@RequestPart("newImage") MultipartFile file
//            , Model model
    ) {
        PictureWork picWorker = new PictureWork(new Picture());
        picWorker.managePicture(file);
        pictureDao.save(picWorker.getPicture());
        return "redirect:/pictures";
    }


    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public @ResponseBody String provideUploadInfo() {
        return "Вы можете загружать файл с использованием того же URL.";
    }


    @Autowired
    public void setPictureDAO(DAO<Picture> pictureDao) {
        this.pictureDao = pictureDao;
        this.pictureDao.setCurrentClass(Picture.class);
    }
}
