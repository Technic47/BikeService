package ru.kuznetsov.bikeService.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.users.UserModel;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;
    @Value("${adminUser.name}")
    private String adminName;
    @Value("${adminUser.pass}")
    private String adminPass;

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

    public void adminSave() {
        UserModel adminUser = new UserModel();
//        adminUser.setUsername(adminName);
//        adminUser.setPassword(adminPass);
        adminUser.setUsername("admin");
        adminUser.setPassword("1999");
        this.userService.createAdmin(adminUser);
    }
}
