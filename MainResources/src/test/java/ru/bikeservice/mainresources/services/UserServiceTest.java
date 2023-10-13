package ru.bikeservice.mainresources.services;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import ru.bikeservice.mainresources.models.lists.UserEntity;
import ru.bikeservice.mainresources.models.users.UserModel;
import ru.bikeservice.mainresources.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static ru.bikeservice.mainresources.TestCredentials.*;
import static ru.bikeservice.mainresources.models.users.UserRole.ROLE_ADMIN;
import static ru.bikeservice.mainresources.models.users.UserRole.ROLE_USER;

@SpringBootTest
@TestPropertySource("/application-test.properties")
@Sql(value = {"/SQL_scripts/clean-after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceTest {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private UserService userService;
    @Autowired
    private UserRepository repository;
    private UserModel userModel;

    @BeforeEach
    void setUp() {
        this.userService = new UserService(repository, passwordEncoder);
        userModel = TEST_USER;
        List<UserEntity> itemList = new ArrayList<>();
        itemList.add(new UserEntity("Test1", 1L));
        itemList.add(new UserEntity("Test2", 2L));
        itemList.add(new UserEntity("Test3", 3L));
        itemList.add(new UserEntity("Test4", 4L));
        userModel.setCreatedItems(itemList);
    }

    @Test
    @Order(1)
    void createUser() {
        assertTrue(userService.createUser(userModel));

        UserModel checkUser = userService.findByUsernameOrNull(TEST_NAME);

        assertNotNull(checkUser);
        assertTrue(checkUser.isActive());
        assertNotEquals(TEST_PASS, checkUser.getPassword());
        assertTrue(checkUser.getAuthorities().contains(ROLE_USER));
        assertFalse(checkUser.getAuthorities().contains(ROLE_ADMIN));
    }

    @Test
    @Order(2)
    void createAdmin() {
        userService.createAdmin(userModel);

        UserModel checkUser = userService.findByUsernameOrNull(TEST_NAME);

        assertNotNull(checkUser);
        assertTrue(checkUser.isActive());
        assertNotEquals(TEST_PASS, checkUser.getPassword());
        assertTrue(checkUser.getAuthorities().contains(ROLE_ADMIN));
        assertFalse(checkUser.getAuthorities().contains(ROLE_USER));
    }

    @Test
    @Order(3)
    void createUserFalse() {
        assertTrue(userService.createUser(userModel));
        assertFalse(userService.createUser(userModel));
    }

    @Test
    @Order(4)
    void addCreatedItem() {
        userService.createUser(userModel);
        UserEntity entity = new UserEntity(TEST_NAME, TEST_ID);
        UserModel checkUser = userService.findByUsernameOrNull(TEST_NAME);
        userService.addCreatedItem(checkUser, entity);
        checkUser = userService.findByUsernameOrNull(TEST_NAME);

        assertEquals(1, checkUser.getCreatedItems().size());
        assertTrue(checkUser.getCreatedItems().contains(entity));
    }

    @Test
    @Order(5)
    void delCreatedItem() {
        userService.createUser(userModel);
        UserEntity entity = new UserEntity(TEST_NAME, TEST_ID);
        UserModel checkUser = userService.findByUsernameOrNull(TEST_NAME);
        userService.addCreatedItem(checkUser, entity);
        userService.delCreatedItem(checkUser, entity);

        assertEquals(0, checkUser.getCreatedItems().size());
    }

    @Test
    @Order(6)
    void findByName() {
        userService.createUser(TEST_USER);
        UserModel result = userService.findByUsernameOrNull(TEST_NAME);

        assertEquals(TEST_USER.getName(), result.getName());
        assertNotNull(result.getId());
        assertNotNull(result.getPassword());
    }

    @Test
    @Order(7)
    void findByNameNull() {
        assertNull(userService.findByUsernameOrNull(TEST_NAME));
    }

    @Test
    @Order(8)
    void userToAdmin(){
        userService.createUser(TEST_USER);
        UserModel user = userService.findByUsernameOrNull(TEST_NAME);
        userService.userToAdmin(user.getId());

        user = userService.findByUsernameOrNull(TEST_NAME);
        assertThat(user.getAuthorities()).hasSize(2).contains(ROLE_ADMIN);
    }

    @Test
    @Order(9)
    void adminToUser(){
        userService.createAdmin(TEST_USER);
        UserModel user = userService.findByUsernameOrNull(TEST_NAME);
        userService.adminToUser(user.getId());

        user = userService.findByUsernameOrNull(TEST_NAME);
        assertThat(user.getAuthorities()).hasSize(0).doesNotContain(ROLE_ADMIN);
    }

    @Test
    @Order(10)
    void update(){
        userService.createUser(TEST_USER);
        UserModel newModel = new UserModel("NewName", "NewEmail", "NewPass");
        UserModel user = userService.findByUsernameOrNull(TEST_NAME);
        userService.update(user, newModel);

        user = userService.findByUsernameOrNull("NewName");

        assertEquals("NewName", user.getUsername());
        assertNotEquals(TEST_PASS, user.getPassword());
    }
}