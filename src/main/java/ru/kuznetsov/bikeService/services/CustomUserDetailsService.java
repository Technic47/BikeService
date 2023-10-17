package ru.kuznetsov.bikeService.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.bikeservice.mainresources.models.users.UserModel;

import java.sql.SQLException;

import static ru.kuznetsov.bikeService.config.SpringConfig.ADMIN_NAME;
import static ru.kuznetsov.bikeService.config.SpringConfig.ADMIN_PASS;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    // https://www.baeldung.com/registration-verify-user-by-email
    private final UserService userService;


    @Autowired
    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
        this.adminSave();
    }

    /**
     * Find user by email and return if it is present.
     *
     * @param userEmail email to search
     * @return UserDetails object
     * @throws UsernameNotFoundException
     */
    @Override
    public UserModel loadUserByUsername(String userEmail) throws RuntimeException {
        UserModel user = userService.findByEmailOrNull(userEmail);
        if (user == null) {
            throw new UsernameNotFoundException(
                    "User not found: " + userEmail);
        }
        boolean enabled = user.isEnabled();
        if (!enabled) {
            throw new RuntimeException("User is disabled: " + userEmail);
        }
        return user;
    }


    /**
     * Creates built-in admin user and send it to DB.
     * Deletes admin credentials.
     */
    @Transactional(isolation = Isolation.READ_COMMITTED,
            rollbackFor = {SQLException.class, RuntimeException.class})
    void adminSave() {
        UserModel adminUser = new UserModel();
        adminUser.setUsername(ADMIN_NAME);
        adminUser.setEmail(ADMIN_NAME);
        adminUser.setPassword(ADMIN_PASS);
        adminUser.setEnabled(true);
        this.userService.createAdmin(adminUser);
        ADMIN_NAME = "***";
        ADMIN_PASS = "***";
    }
}
