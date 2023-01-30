package ru.kuznetsov.bikeService.models.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;
import ru.kuznetsov.bikeService.services.UserService;

@Component
public class SessionUser {
    private UserModel user;
    private UserService userService;

    @Bean
    @SessionScope
    public UserModel getCurrentUser() {
        if (user == null) {
            user = userService.findByName(SecurityContextHolder.getContext().getAuthentication().getName());
        }
        return user;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
