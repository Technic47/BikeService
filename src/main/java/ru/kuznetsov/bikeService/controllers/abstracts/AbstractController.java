//package ru.kuznetsov.bikeService.controllers.abstracts;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//import ru.kuznetsov.bikeService.models.users.UserModel;
//import ru.kuznetsov.bikeService.services.PictureService;
//import ru.kuznetsov.bikeService.services.UserService;
//
//import java.security.Principal;
//
//@Component
//public abstract class AbstractController {
//    protected UserService userService;
//    protected PictureService pictureDao;
//    protected UserModel user;
//
//    @Bean
//    public UserModel setUpUser() {
//        return new UserModel();
//    }
//
//    @Autowired
//    public void setUser(UserModel user) {
//        this.user = user;
//    }
//
//    @Autowired
//    public void setUserService(UserService userService) {
//        this.userService = userService;
//    }
//
//    @Autowired
//    public void setPictureDao(PictureService pictureDao) {
//        this.pictureDao = pictureDao;
//    }
//}
