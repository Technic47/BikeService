package ru.kuznetsov.bikeService.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import ru.kuznetsov.bikeService.repositories.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ru.kuznetsov.bikeService.TestCredentials.*;

@SpringBootTest
@TestPropertySource("/application-test.properties")
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
        TEST_USER.setEnabled(true);
        doReturn(TEST_USER)
                .when(userRepository)
                .findByEmail(TEST_NAME);
        UserDetails result = detailsService.loadUserByUsername(TEST_NAME);

        assertEquals(TEST_USER, result);
        verify(userRepository, times(1)).findByEmail(TEST_NAME);
    }

    @Test
    void loadUserByUsernameNull() {
        doReturn(null)
                .when(userRepository)
                .findByEmail(TEST_EMAIL);

        assertThrows(UsernameNotFoundException.class, () -> detailsService.loadUserByUsername(TEST_EMAIL));
    }
}