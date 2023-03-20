package ru.kuznetsov.bikeService.models.users;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.kuznetsov.bikeService.models.lists.UserEntity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static ru.kuznetsov.bikeService.TestCridentials.*;

class UserModelTest {

    private UserModel userModel;

    @BeforeEach
    void setUp(){
        this.userModel = new UserModel();
        this.userModel.setId(TEST_ID);
        this.userModel.setUsername(TEST_NAME);
        this.userModel.setActive(true);
        this.userModel.setPassword(TEST_PASS);
    }

    @Test
    void userModelTest() {
        Set<UserRole> status = new HashSet<>();
        status.add(UserRole.ROLE_USER);
        this.userModel.setStatus(status);

        List<UserEntity> createdItems = new ArrayList<>();
        createdItems.add(new UserEntity("Document", 1L));
        createdItems.add(new UserEntity("Fastener", 2L));
        createdItems.add(new UserEntity("Document", 11L));
        this.userModel.setCreatedItems(createdItems);

        assertEquals(TEST_ID, this.userModel.getId());
        assertEquals(TEST_NAME, this.userModel.getUsername());
        assertTrue(this.userModel.isActive());
        assertEquals(TEST_PASS, this.userModel.getPassword());
        assertThat(this.userModel.getStatus()).size().isEqualTo(1);
        assertEquals(status, userModel.getStatus());
    }

    @Test
    void userModelListTest() {
        List<UserEntity> createdItems = new ArrayList<>();
        createdItems.add(new UserEntity("Document", 1L));
        createdItems.add(new UserEntity("Fastener", 2L));
        createdItems.add(new UserEntity("Document", 11L));
        this.userModel.setCreatedItems(createdItems);

        assertEquals(createdItems, userModel.getCreatedItems());
    }

    @Test
    void userModelEqualsTest() {
        UserModel userModel2 = new UserModel();
        userModel2.setId(TEST_ID);
        userModel2.setUsername(TEST_NAME);
        userModel2.setActive(true);
        userModel2.setPassword("TestPass");

        assertEquals(userModel, userModel2);
        userModel2.setPassword("12345");
        assertEquals(userModel, userModel2);
        userModel2.setUsername("qwqw");
        assertNotEquals(userModel, userModel2);
        userModel2.setUsername(TEST_NAME);
        userModel2.setId(22L);
        assertNotEquals(userModel, userModel2);
    }
}