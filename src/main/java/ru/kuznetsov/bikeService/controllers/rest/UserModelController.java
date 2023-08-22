package ru.kuznetsov.bikeService.controllers.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kuznetsov.bikeService.controllers.abstracts.AbstractController;
import ru.kuznetsov.bikeService.models.users.UserModel;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class UserModelController extends AbstractController {
    @GetMapping
//    @Secured("ROLE_ADMIN")
    public List<UserModel> index() {
     return userService.index();
    }
}
