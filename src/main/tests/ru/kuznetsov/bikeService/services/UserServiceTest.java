package ru.kuznetsov.bikeService.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kuznetsov.bikeService.models.lists.UserEntity;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.models.users.UserRole;
import ru.kuznetsov.bikeService.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.kuznetsov.bikeService.TestCridentials.*;

@SpringBootTest
class UserServiceTest {
    private UserService userService;
    @MockBean
    private UserRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private UserModel userModel;

    @BeforeEach
    void setUp() {
        this.userService = new UserService(repository, passwordEncoder);
        userModel = new UserModel();
        userModel.setUsername(TEST_NAME);
        userModel.setPassword(TEST_PASS);
        List<UserEntity> itemList = new ArrayList<>();
        itemList.add(new UserEntity("Test1", 1L));
        itemList.add(new UserEntity("Test2", 2L));
        itemList.add(new UserEntity("Test3", 3L));
        itemList.add(new UserEntity("Test4", 4L));
        userModel.setCreatedItems(itemList);
    }

    @Test
    void createUser() {
        boolean isCreated = userService.createUser(userModel);

        assertTrue(isCreated);
        assertNotEquals(TEST_PASS, userModel.getPassword());
        assertTrue(userModel.getStatus().contains(UserRole.ROLE_USER));

        verify(repository, times(1)).findByUsername(TEST_NAME);
        verify(repository, times(1)).save(userModel);
    }

    @Test
    void createUserFalse() {
        doReturn(new UserModel())
                .when(repository)
                .findByUsername(TEST_NAME);
        boolean isCreated = userService.createUser(userModel);

        assertFalse(isCreated);

        verify(repository, times(0)).save(userModel);
    }

    @Test
    void addCreatedItem() {
        UserEntity entity = new UserEntity(TEST_NAME, TEST_ID);
        userService.addCreatedItem(this.userModel, entity);

        assertEquals(5, this.userModel.getCreatedItems().size());
        assertTrue(this.userModel.getCreatedItems().contains(entity));
        verify(repository, times(1)).save(userModel);
    }

    @Test
    void delCreatedItem() {
        UserEntity entity = new UserEntity(TEST_NAME, TEST_ID);
        userService.delCreatedItem(this.userModel, entity);

        assertEquals(4, this.userModel.getCreatedItems().size());
        assertFalse(this.userModel.getCreatedItems().contains(entity));
        verify(repository, times(1)).save(userModel);
    }

    @Test
    void findByName() {
        doReturn(new UserModel())
                .when(repository)
                .findByUsername(TEST_NAME);
        UserModel result = userService.findByName(TEST_NAME);

        assertEquals(new UserModel(), result);
        verify(repository, times(1)).findByUsername(TEST_NAME);
    }

    @Test
    void findByNameNull() {
        doReturn(null)
                .when(repository)
                .findByUsername(TEST_NAME);
        UserModel result = userService.findByName(TEST_NAME);

        assertNull(result);
        verify(repository, times(1)).findByUsername(TEST_NAME);
    }
}