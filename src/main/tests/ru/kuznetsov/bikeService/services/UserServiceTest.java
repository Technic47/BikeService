//package ru.kuznetsov.bikeService.services;
//
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.context.TestPropertySource;
//import ru.kuznetsov.bikeService.models.lists.UserEntity;
//import ru.kuznetsov.bikeService.models.users.UserModel;
//import ru.kuznetsov.bikeService.models.users.UserRole;
//import ru.kuznetsov.bikeService.repositories.UserRepository;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//import static ru.kuznetsov.bikeService.TestCredentials.*;
//import static ru.kuznetsov.bikeService.models.users.UserRole.ROLE_ADMIN;
//import static ru.kuznetsov.bikeService.models.users.UserRole.ROLE_USER;
//
//@SpringBootTest
//@TestPropertySource("/application-test.properties")
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//class UserServiceTest {
//    private UserService userService;
//    @MockBean
//    private UserRepository repository;
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//    private UserModel userModel;
//
//    @BeforeEach
//    void setUp() {
//        this.userService = new UserService(repository, passwordEncoder);
//        userModel = TEST_USER;
//        List<UserEntity> itemList = new ArrayList<>();
//        itemList.add(new UserEntity("Test1", 1L));
//        itemList.add(new UserEntity("Test2", 2L));
//        itemList.add(new UserEntity("Test3", 3L));
//        itemList.add(new UserEntity("Test4", 4L));
//        userModel.setCreatedItems(itemList);
//    }
//
//    @Test
//    @Order(1)
//    void createUser() {
//        assertTrue(userService.createUser(userModel));
//        assertTrue(userService.getUserModel().isActive());
//        assertNotEquals(TEST_PASS, userService.getUserModel().getPassword());
//        assertTrue(userService.getUserModel().getAuthorities().contains(ROLE_USER));
//        assertFalse(userService.getUserModel().getAuthorities().contains(ROLE_ADMIN));
//        verify(repository, atLeast(1)).findByUsername(TEST_NAME);
//        verify(repository, atLeast(1)).save(any(UserModel.class));
//    }
//
//    @Test
//    @Order(2)
//    void createAdmin() {
//        userService.createAdmin(userModel);
//        assertTrue(userService.getUserModel().isActive());
//        assertNotEquals(TEST_PASS, userService.getUserModel().getPassword());
//        assertTrue(userService.getUserModel().getAuthorities().contains(ROLE_ADMIN));
//        assertFalse(userService.getUserModel().getAuthorities().contains(ROLE_USER));
//        verify(repository, atLeast(1)).findByUsername(TEST_NAME);
//        verify(repository, atLeast(1)).save(any(UserModel.class));
//    }
//
//    @Test
//    @Order(3)
//    void createUserFalse() {
//        doReturn(new UserModel())
//                .when(repository)
//                .findByUsername(TEST_NAME);
//
//        assertFalse(userService.createUser(userModel));
//
//        verify(repository, times(0)).save(userModel);
//    }
//
//    @Test
//    @Order(4)
//    void addCreatedItem() {
//        UserEntity entity = new UserEntity(TEST_NAME, TEST_ID);
//        userService.addCreatedItem(this.userModel, entity);
//
//        assertEquals(5, this.userModel.getCreatedItems().size());
//        assertTrue(this.userModel.getCreatedItems().contains(entity));
//        verify(repository, times(1)).save(userModel);
//    }
//
//    @Test
//    @Order(5)
//    void delCreatedItem() {
//        UserEntity entity = new UserEntity(TEST_NAME, TEST_ID);
//        userService.delCreatedItem(this.userModel, entity);
//
//        assertEquals(4, this.userModel.getCreatedItems().size());
//        assertFalse(this.userModel.getCreatedItems().contains(entity));
//        verify(repository, times(1)).save(userModel);
//    }
//
//    @Test
//    @Order(6)
//    void findByName() {
//        doReturn(new UserModel())
//                .when(repository)
//                .findByUsername(TEST_NAME);
//        UserModel result = userService.findByName(TEST_NAME);
//
//        assertEquals(new UserModel(), result);
//        verify(repository, times(1)).findByUsername(TEST_NAME);
//    }
//
//    @Test
//    @Order(7)
//    void findByNameNull() {
//        doReturn(null)
//                .when(repository)
//                .findByUsername(TEST_NAME);
//        UserModel result = userService.findByName(TEST_NAME);
//
//        assertNull(result);
//        verify(repository, times(1)).findByUsername(TEST_NAME);
//    }
//
//    @Test
//    @Order(8)
//    void userToAdmin(){
//        UserModel user = new UserModel();
//        Set<UserRole> roles = new HashSet<>();
//        roles.add(ROLE_USER);
//        user.setAuthorities(roles);
//        user.setId(TEST_ID);
//        doReturn(user)
//                .when(repository)
//                .getReferenceById(TEST_ID);
//
//        userService.userToAdmin(TEST_ID);
//
//        assertThat(user.getAuthorities()).hasSize(2).contains(ROLE_ADMIN);
//        verify(repository, times(1)).save(user);
//    }
//
//    @Test
//    @Order(9)
//    void adminToUser(){
//        UserModel user = new UserModel();
//        Set<UserRole> roles = new HashSet<>();
//        roles.add(ROLE_ADMIN);
//        roles.add(ROLE_USER);
//        user.setAuthorities(roles);
//        user.setId(TEST_ID);
//        doReturn(user)
//                .when(repository)
//                .getReferenceById(TEST_ID);
//
//        userService.adminToUser(TEST_ID);
//
//        assertThat(user.getAuthorities()).hasSize(1).doesNotContain(ROLE_ADMIN);
//        verify(repository, times(1)).save(user);
//    }
//
//    @Test
//    @Order(10)
//    void update(){
//        UserModel newModel = new UserModel("NewName", "NewPass");
//        userService.update(this.userModel, newModel);
//
//        assertEquals("NewName", this.userModel.getUsername());
//        assertNotEquals(TEST_PASS, this.userModel.getPassword());
//        verify(repository).save(any(UserModel.class));
//    }
//}