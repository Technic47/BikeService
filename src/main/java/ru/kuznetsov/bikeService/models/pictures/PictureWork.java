package ru.kuznetsov.bikeService.models.pictures;

import org.imgscalr.Scalr;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

import static ru.kuznetsov.bikeService.config.SpringConfig.UPLOAD_PATH;

@Component
public class PictureWork {
    private Picture picture;
    private final static Integer PICTURE_WIDTH = 400;
    private final static Integer PICTURE_HEIGHT = 300;
    private final static Integer PREVIEW_WIDTH = 64;
    private final static Integer PREVIEW_HEIGHT = 64;

    public PictureWork() {
        this.picture = new Picture();
    }


    public void managePicture(MultipartFile file) {
        try {
            BufferedImage uploadedImage = ImageIO.read(file.getInputStream());
            BufferedImage bigImageOut = resizePicture(uploadedImage, PICTURE_WIDTH, PICTURE_HEIGHT);
            BufferedImage smallImageOut = resizePicture(uploadedImage, PREVIEW_WIDTH, PREVIEW_HEIGHT);
            File uploadDir = new File(UPLOAD_PATH);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            ImageIO.write(bigImageOut, "png", new File(UPLOAD_PATH, file.getOriginalFilename()));
            ImageIO.write(smallImageOut, "png", new File(UPLOAD_PATH + "/preview", file.getOriginalFilename()));
            this.picture.setName(file.getOriginalFilename());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private BufferedImage resizePicture(BufferedImage uploadedImage,
                                        int newWidth, int newHeight) {
        BufferedImage imageOut;
        if (uploadedImage.getWidth() > uploadedImage.getHeight()) {
            imageOut = Scalr.resize(uploadedImage,
                    Scalr.Method.SPEED, Scalr.Mode.FIT_TO_WIDTH, newWidth);
        } else {
            imageOut = Scalr.resize(uploadedImage,
                    Scalr.Method.SPEED, Scalr.Mode.FIT_TO_HEIGHT, newHeight);
        }
        return imageOut;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }
}
