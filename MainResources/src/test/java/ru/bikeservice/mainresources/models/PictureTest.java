package ru.bikeservice.mainresources.models;

import org.junit.jupiter.api.Test;
import ru.bikeservice.mainresources.models.pictures.Picture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static ru.bikeservice.mainresources.TestCredentials.TEST_ID;
import static ru.bikeservice.mainresources.TestCredentials.TEST_NAME;

class PictureTest {
    @Test
    void pictureTest() {
        Picture picture = new Picture();
        picture.setId(TEST_ID);
        picture.setName(TEST_NAME);
        assertEquals(TEST_ID, picture.getId());
        assertEquals(TEST_NAME, picture.getName());

        Picture picture2 = new Picture();
        picture2.setId(TEST_ID);
        picture2.setName(TEST_NAME);

        assertEquals(picture, picture2);
        picture2.setName("qwe");
        assertNotEquals(picture, picture2);
        picture2.setName(TEST_NAME);
        picture2.setId(111L);
        assertNotEquals(picture, picture2);
    }
}