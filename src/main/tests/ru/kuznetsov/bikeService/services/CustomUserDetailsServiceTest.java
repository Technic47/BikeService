package ru.kuznetsov.bikeService.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.repositories.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ru.kuznetsov.bikeService.TestCredentials.TEST_NAME;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class CustomUserDetailsServiceTest {
    private CustomUserDetailsService detailsService;
    private UserService userService;
    private JavaMailSender mailSender;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        this.userService = new UserService(userRepository, passwordEncoder, mailSender);
        this.detailsService = new CustomUserDetailsService(userService);
    }

    @Test
    void loadUserByUsername() {
        doReturn(new UserModel())
                .when(userRepository)
                .findByUsername(TEST_NAME);
        UserDetails result = detailsService.loadUserByUsername(TEST_NAME);

        assertEquals(new UserModel(), result);
        verify(userRepository, times(1)).findByUsername(TEST_NAME);
    }

    @Test
    void loadUserByUsernameNull() {
        doReturn(null)
                .when(userRepository)
                .findByUsername(TEST_NAME);

        assertThrows(UsernameNotFoundException.class, () -> detailsService.loadUserByUsername(TEST_NAME));
    }
}