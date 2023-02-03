package ru.kuznetsov.bikeService.controllers.abstracts;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kuznetsov.bikeService.services.PictureService;
import ru.kuznetsov.bikeService.services.UserService;

@Component
public abstract class AbstractController {
    final static Logger logger = LoggerFactory.getLogger("BikeServiceLogger");
    protected UserService userService;
    protected PictureService pictureDao;


    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setPictureDao(PictureService pictureDao) {
        this.pictureDao = pictureDao;
    }
}
