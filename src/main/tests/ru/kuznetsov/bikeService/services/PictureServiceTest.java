package ru.kuznetsov.bikeService.services;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.multipart.MultipartFile;
import ru.kuznetsov.bikeService.models.pictures.Picture;
import ru.kuznetsov.bikeService.repositories.PictureRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static ru.kuznetsov.bikeService.TestCredentials.*;
import static ru.kuznetsov.bikeService.config.SpringConfig.UPLOAD_PATH;

@SpringBootTest
@TestPropertySource("/application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PictureServiceTest {
    private PictureService pictureService;
    @MockBean
    private PictureRepository repository;
    private Picture defaultPicture;
    public static final String PATH_WIDE_FILE = "src/main/testresources/testImage.jpg";
    private static final String PATH_TALL_FILE = "src/main/testresources/testImage2.jpg";
    @Autowired
    @Qualifier("AdditionExecutor")
    private ExecutorService additionalExecutor;
    @Autowired
    @Qualifier("MainExecutor")
    private ExecutorService mainExecutor;
    private MultipartFile multipartFile;

    @AfterAll
    static void cleanUp() {
        List<File> filesToDel = new ArrayList<>();
        filesToDel.add(new File(UPLOAD_PATH + "/testImage.jpg"));
        filesToDel.add(new File(UPLOAD_PATH + "/testImage2.jpg"));
        filesToDel.add(new File(UPLOAD_PATH + "/preview/testImage.jpg"));
        filesToDel.add(new File(UPLOAD_PATH + "/preview/testImage2.jpg"));
        filesToDel.forEach(file -> {
            if (file.exists()) {
                file.delete();
            }
        });
    }

    @BeforeEach
    void setUp() {
        this.pictureService = new PictureService(repository, additionalExecutor);
        this.pictureService.setMainExecutor(this.mainExecutor);
        this.defaultPicture = new Picture(TEST_ID, "testImage.jpg");
    }

    private MultipartFile getMultipartFile(String path) {
        MultipartFile multipartFile = null;
        try {
            File initialFile = new File(path);
            InputStream targetStream1 = new FileInputStream(initialFile);
            multipartFile = new MockMultipartFile(TEST_NAME, initialFile.getName(), "image/jpeg", targetStream1);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return multipartFile;
    }

    @Test
    @Order(1)
    void managePictureWide() throws IOException, InterruptedException {
        this.multipartFile = this.getMultipartFile(PATH_WIDE_FILE);
        this.pictureService.save(multipartFile);

        File convertedImage = new File(UPLOAD_PATH + "/testImage.jpg");
        File previewImage = new File(UPLOAD_PATH + "/preview/testImage.jpg");
        Thread.sleep(500);
        BufferedImage buffConvertedImage = ImageIO.read(convertedImage);
        BufferedImage buffPreview = ImageIO.read(previewImage);

        assertTrue(new File(UPLOAD_PATH + "/testImage.jpg").exists());
        assertTrue(new File(UPLOAD_PATH + "/preview/testImage.jpg").exists());

        assertEquals(400, buffConvertedImage.getWidth());
        assertEquals(64, buffPreview.getWidth());
    }

    @Test
    @Order(2)
    void managePictureTall() throws IOException, InterruptedException {
        this.multipartFile = this.getMultipartFile(PATH_TALL_FILE);
        this.pictureService.save(multipartFile);

        File convertedImage = new File(UPLOAD_PATH + "/testImage2.jpg");
        File previewImage = new File(UPLOAD_PATH + "/preview/testImage2.jpg");
        Thread.sleep(500);
        assertTrue(new File(UPLOAD_PATH + "/testImage2.jpg").exists());
        assertTrue(new File(UPLOAD_PATH + "/preview/testImage2.jpg").exists());

        BufferedImage buffConvertedImage = ImageIO.read(convertedImage);
        BufferedImage buffPreview = ImageIO.read(previewImage);


        assertEquals(300, buffConvertedImage.getHeight());
        assertEquals(64, buffPreview.getHeight());
    }

    @Test
    @Order(3)
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
    @Order(4)
    void delete() {
        doReturn(Optional.of(defaultPicture))
                .when(repository)
                .findById(TEST_ID);

        pictureService.save(getDefaultMultipartFile());

//        this.pictureService.save(this.getMultipartFile(PATH_WIDE_FILE));
//        this.pictureService.save(this.getMultipartFile(PATH_TALL_FILE));

        File fileWide = new File(UPLOAD_PATH + "/testImage.jpg");
//        File fileTall = new File(UPLOAD_PATH + "/testImage2.jpg");
        File filePreviewWide = new File(UPLOAD_PATH + "/preview/testImage.jpg");
//        File filePreviewTall = new File(UPLOAD_PATH + "/preview/testImage2.jpg");

        assertTrue(fileWide.exists());
//        assertTrue(fileTall.exists());
        assertTrue(filePreviewWide.exists());
//        assertTrue(filePreviewTall.exists());

        pictureService.delete(TEST_ID);
//        pictureService.delete(5L);

        assertFalse(fileWide.exists());
        assertFalse(filePreviewWide.exists());
//        assertFalse(fileTall.exists());
//        assertFalse(filePreviewTall.exists());
        verify(repository).deleteById(TEST_ID);
    }

    @Test
    @Order(5)
    void deleteFail() {
        doReturn(Optional.empty())
                .when(repository)
                .findById(TEST_ID);

        pictureService.delete(TEST_ID);

        verify(repository, times(0)).deleteById(TEST_ID);
    }
}