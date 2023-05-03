package ru.kuznetsov.bikeService.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.users.UserModel;

import static ru.kuznetsov.bikeService.config.SpringConfig.ADMIN_NAME;
import static ru.kuznetsov.bikeService.config.SpringConfig.ADMIN_PASS;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;


    @Autowired
    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
        this.adminSave();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = userService.findByName(username);
        if (userDetails == null) {
            throw new UsernameNotFoundException(username);
        }
        return userDetails;
    }

    private void adminSave() {
        UserModel adminUser = new UserModel();
        adminUser.setUsername(ADMIN_NAME);
        adminUser.setPassword(ADMIN_PASS);
        this.userService.createAdmin(adminUser);
        ADMIN_NAME = "***";
        ADMIN_PASS = "***";
    }
}
