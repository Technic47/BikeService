package ru.bikeservice.mainresources.models.users;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static ru.bikeservice.mainresources.TestCredentials.*;
import static ru.bikeservice.mainresources.models.users.UserRole.ROLE_USER;

class UserBuilderTest {
    private final PasswordEncoder encoder = new BCryptPasswordEncoder();
    private UserBuilder builder = new UserBuilder();

    @Test
    void setName() {
        builder.setName(TEST_NAME);

        assertEquals(builder.build().getUsername(), TEST_NAME);
    }

    @Test
    void setPassword() {
        builder.setPassword(TEST_PASS);

        assertEquals(builder.build().getPassword(), TEST_PASS);
    }

    @Test
    void encodePassword() {
        builder.setPassword(TEST_PASS).encodePassword(encoder);

        assertNotEquals(builder.build().getPassword(), TEST_PASS);
    }

    @Test
    void setRole() {
        builder.addRole(ROLE_USER);

        assertTrue(builder.build().getAuthorities().contains(ROLE_USER));
    }

    @Test
    void setActive() {
        builder.setActive(true);

        assertTrue(builder.build().isActive());
    }

    @Test
    void build() {
        builder = new UserBuilder(TEST_USER.getName(), TEST_USER.getPassword());
        UserModel user = builder.build();

        assertEquals(user.getUsername(), TEST_NAME);
        assertEquals(user.getPassword(), TEST_PASS);
    }
}