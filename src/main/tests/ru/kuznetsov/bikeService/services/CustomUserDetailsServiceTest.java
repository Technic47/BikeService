package ru.kuznetsov.bikeService.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.repositories.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static ru.kuznetsov.bikeService.TestCridentials.TEST_NAME;

@SpringBootTest
class CustomUserDetailsServiceTest {
    private CustomUserDetailsService detailsService;
    @MockBean
    private UserRepository repository;

    @BeforeEach
    void setUp() {
        this.detailsService = new CustomUserDetailsService(repository);
    }

    @Test
    void loadUserByUsername() {
        doReturn(new UserModel())
                .when(repository)
                .findByUsername(TEST_NAME);
        UserDetails result = detailsService.loadUserByUsername(TEST_NAME);

        assertEquals(new UserModel(), result);
        verify(repository, times(1)).findByUsername(TEST_NAME);
    }

    @Test
    void loadUserByUsernameNull() {
        doReturn(null)
                .when(repository)
                .findByUsername(TEST_NAME);

        assertThrows(UsernameNotFoundException.class, () -> detailsService.loadUserByUsername(TEST_NAME));
    }
}