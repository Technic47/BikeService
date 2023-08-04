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
     * @param userEmail search parameter
     * @return UserDetails object
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
//        boolean enabled = true;
//        boolean accountNonExpired = true;
//        boolean credentialsNonExpired = true;
//        boolean accountNonLocked = true;
        try {
            UserModel user = userService.findByEmail(userEmail);
            if (user == null) {
                throw new UsernameNotFoundException(
                        "No user found with username: " + userEmail);
            }
            boolean enabled = user.isEnabled();
            if (!enabled) {
                throw new RuntimeException("User is not activated: " + userEmail);
            }
            return user;

//            return new org.springframework.security.core.userdetails.User(
//                    user.getEmail(),
//                    user.getPassword().toLowerCase(),
//                    user.isEnabled(),
//                    accountNonExpired,
//                    credentialsNonExpired,
//                    accountNonLocked,
//                    user.getAuthorities());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
