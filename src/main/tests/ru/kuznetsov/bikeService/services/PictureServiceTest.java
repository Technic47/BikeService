package ru.kuznetsov.bikeService.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.kuznetsov.bikeService.models.pictures.Picture;
import ru.kuznetsov.bikeService.repositories.PictureRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static ru.kuznetsov.bikeService.TestCridentials.TEST_ID;
import static ru.kuznetsov.bikeService.TestCridentials.TEST_NAME;

@SpringBootTest
class PictureServiceTest {
    private PictureService pictureService;
    @MockBean
    private PictureRepository repository;
    private Picture defaultPicture;

    @BeforeEach
    void setUp() {
        this.pictureService = new PictureService(repository);
        this.defaultPicture = new Picture(TEST_ID, TEST_NAME);
    }

    @Test
    void update() {
        Picture newPicture = new Picture(defaultPicture.getId(), "test2");
        doReturn(Optional.of(defaultPicture))
                .when(repository)
                .findById(TEST_ID);
        pictureService.update(TEST_ID, newPicture);

        assertEquals("test2", this.defaultPicture.getName());
        verify(repository, times(1)).findById(TEST_ID);
        verify(repository, times(1)).save(this.defaultPicture);
    }
}