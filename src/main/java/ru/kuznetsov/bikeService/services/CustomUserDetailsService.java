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

    /**
     * Find user by userName and return if it is present.
     *
     * @param username search parameter
     * @return UserDetails object
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel model = userService.findByName(username);
        if (model == null) {
            throw new UsernameNotFoundException(username);
        }
//        USERMODEL = model;
        return model;
    }

    /**
     * Creates built-in admin user and send it to DB.
     * Deletes admin credentials.
     */
    private void adminSave() {
        UserModel adminUser = new UserModel();
        adminUser.setUsername(ADMIN_NAME);
        adminUser.setPassword(ADMIN_PASS);
        this.userService.createAdmin(adminUser);
        ADMIN_NAME = "***";
        ADMIN_PASS = "***";
    }
}
