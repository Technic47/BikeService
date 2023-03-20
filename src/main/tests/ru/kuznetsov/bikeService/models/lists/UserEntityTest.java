package ru.kuznetsov.bikeService.models.lists;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class UserEntityTest {

    @Test
    void userEntityTest() {
        UserEntity entity1 = new UserEntity();
        entity1.setType("Document");
        entity1.setId(1L);
        UserEntity entity2 = new UserEntity("Document", 1L);
        assertEquals("Document", entity1.getType());
        assertEquals(1L, entity1.getId());
        assertEquals(entity1, entity2);
        entity2.setId(2L);
        assertNotEquals(entity1, entity2);
    }
}