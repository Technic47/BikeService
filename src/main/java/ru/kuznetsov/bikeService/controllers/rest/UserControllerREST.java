package ru.kuznetsov.bikeService.controllers.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kuznetsov.bikeService.controllers.abstracts.AbstractController;
import ru.kuznetsov.bikeService.models.dto.UserDto;
import ru.kuznetsov.bikeService.models.users.UserModel;

import java.security.Principal;

@RestController
@RequestMapping("/api/users")
public class UserControllerREST extends AbstractController {
    @GetMapping()
    public UserDto getUser(Principal principal){
        String name = principal.getName();
        UserModel findUser = userService.findByUsernameOrNull(name);
//        UserModel userModel = userService.getById(id);
        return new UserDto(findUser);
    }
}
