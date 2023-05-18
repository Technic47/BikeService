package ru.kuznetsov.bikeService.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import ru.kuznetsov.bikeService.models.pictures.Picture;
import ru.kuznetsov.bikeService.models.pictures.PictureWork;
import ru.kuznetsov.bikeService.repositories.PictureRepository;

import java.io.File;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.kuznetsov.bikeService.TestCredentials.TEST_ID;
import static ru.kuznetsov.bikeService.TestCredentials.getMultipartFile;
import static ru.kuznetsov.bikeService.config.SpringConfig.UPLOAD_PATH;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class PictureServiceTest {
    private PictureService pictureService;
    @MockBean
    private PictureRepository repository;
    private Picture defaultPicture;

    @BeforeEach
    void setUp() {
        this.pictureService = new PictureService(repository);
        this.pictureService.setPictureWork(new PictureWork());
        this.defaultPicture = new Picture(TEST_ID, "testImage.jpg");
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

    @Test
    void delete() {
        doReturn(Optional.of(defaultPicture))
                .when(repository)
                .findById(TEST_ID);

        pictureService.save(getMultipartFile());
        File file = new File(UPLOAD_PATH + "/testImage.jpg");

        assertTrue(file.exists());

        pictureService.delete(TEST_ID);
        File filePreview = new File(UPLOAD_PATH + "/preview/testImage.jpg");

        assertFalse(file.exists());
        assertFalse(filePreview.exists());
        verify(repository).deleteById(TEST_ID);
    }

    @Test
    void deleteFail(){
        doReturn(Optional.empty())
                .when(repository)
                .findById(TEST_ID);

        pictureService.delete(TEST_ID);

        verify(repository, times(0)).deleteById(TEST_ID);
    }
}