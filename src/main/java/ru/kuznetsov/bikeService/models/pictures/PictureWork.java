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

    /**
     * Processes MultipartFile. Take out picture, resize it and save to UPLOAD_PATH.
     *
     * @param file file with picture to save.
     */
    public void managePicture(MultipartFile file) {
        try {
            BufferedImage uploadedImage = ImageIO.read(file.getInputStream());
            BufferedImage bigImageOut = resizePicture(uploadedImage, PICTURE_WIDTH, PICTURE_HEIGHT);
            BufferedImage smallImageOut = resizePicture(uploadedImage, PREVIEW_WIDTH, PREVIEW_HEIGHT);

            this.dirCheck();

            this.writeFile(bigImageOut, new File(UPLOAD_PATH, file.getOriginalFilename()));
            this.writeFile(smallImageOut, new File(UPLOAD_PATH + "/preview", file.getOriginalFilename()));

            this.picture.setName(file.getOriginalFilename());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Convert image to specified width and height.
     * Checks if image is tall or wide and fits it to size.
     *
     * @param uploadedImage image for conversion.
     * @param newWidth      target width.
     * @param newHeight     target height.
     * @return - converted image.
     */
    private BufferedImage resizePicture(
            BufferedImage uploadedImage, int newWidth, int newHeight) {
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

    private void dirCheck() {
        File uploadDir = new File(UPLOAD_PATH);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
    }

    private void writeFile(BufferedImage image, File file) throws Exception {
        try {
            ImageIO.write(image, "png", file);
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }
}
