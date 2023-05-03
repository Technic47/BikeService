package ru.kuznetsov.bikeService.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.repositories.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ru.kuznetsov.bikeService.TestCridentials.TEST_NAME;

@SpringBootTest
class CustomUserDetailsServiceTest {
    private CustomUserDetailsService detailsService;

    private UserService userService;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        this.userService = new UserService(userRepository, passwordEncoder);
        this.detailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByUsername() {
        doReturn(new UserModel())
                .when(userService)
                .findByName(TEST_NAME);
        UserDetails result = detailsService.loadUserByUsername(TEST_NAME);

        assertEquals(new UserModel(), result);
        verify(userService, times(1)).findByName(TEST_NAME);
    }

    @Test
    void loadUserByUsernameNull() {
        doReturn(null)
                .when(userService)
                .findByName(TEST_NAME);

        assertThrows(UsernameNotFoundException.class, () -> detailsService.loadUserByUsername(TEST_NAME));
    }
}