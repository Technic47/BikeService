package ru.kuznetsov.bikeService.controllers.pictures;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import ru.kuznetsov.bikeService.models.Picture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static ru.kuznetsov.bikeService.TestCridentials.TEST_NAME;
import static ru.kuznetsov.bikeService.config.SpringConfig.UPLOAD_PATH;

class PictureWorkTest {
    private static final String PATH_WIDE_FILE = "src/main/testresources/testImage.jpg";
    private static final String PATH_TALL_FILE = "src/main/testresources/testImage2.jpg";
    private PictureWork pictureWork;
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
        this.pictureWork = new PictureWork(new Picture());
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
    void managePictureWide() throws IOException {
        this.multipartFile = this.getMultipartFile(PATH_WIDE_FILE);
        this.pictureWork.managePicture(this.multipartFile);

        File convertedImage = new File(UPLOAD_PATH + "/testImage.jpg");
        File previewImage = new File(UPLOAD_PATH + "/preview/testImage.jpg");
        BufferedImage buffConvertedImage = ImageIO.read(convertedImage);
        BufferedImage buffPreview = ImageIO.read(previewImage);

        assertTrue(new File(UPLOAD_PATH + "/testImage.jpg").exists());
        assertTrue(new File(UPLOAD_PATH + "/preview/testImage.jpg").exists());

        assertEquals(400, buffConvertedImage.getWidth());
        assertEquals(64, buffPreview.getWidth());
    }

    @Test
    void managePictureTall() throws IOException {
        this.multipartFile = this.getMultipartFile(PATH_TALL_FILE);
        this.pictureWork.managePicture(this.multipartFile);

        File convertedImage = new File(UPLOAD_PATH + "/testImage2.jpg");
        File previewImage = new File(UPLOAD_PATH + "/preview/testImage2.jpg");
        BufferedImage buffConvertedImage = ImageIO.read(convertedImage);
        BufferedImage buffPreview = ImageIO.read(previewImage);

        assertTrue(new File(UPLOAD_PATH + "/testImage2.jpg").exists());
        assertTrue(new File(UPLOAD_PATH + "/preview/testImage2.jpg").exists());

        assertEquals(300, buffConvertedImage.getHeight());
        assertEquals(64, buffPreview.getHeight());
    }

    @Test
    void getPicture() {
        assertNotNull(this.pictureWork.getPicture());
    }
}