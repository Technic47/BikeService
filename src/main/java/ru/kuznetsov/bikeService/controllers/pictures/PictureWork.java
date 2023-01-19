package ru.kuznetsov.bikeService.controllers.pictures;

import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.kuznetsov.bikeService.models.Picture;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

import static ru.kuznetsov.bikeService.config.SpringConfig.UPLOAD_PATH;

//@Component
public class PictureWork {
//    @Value("${upload.path}")
//    private String uploadPath;
    private final Picture picture;

    public PictureWork(Picture picture) {
        this.picture = picture;
    }


    public void managePicture(MultipartFile file) {
        try {
            BufferedImage uploadedImage = ImageIO.read(file.getInputStream());
            BufferedImage bigImageOut = resizePicture(uploadedImage, 400, 300);
            BufferedImage smallImageOut = resizePicture(uploadedImage, 64, 64);
            File uploadDir = new File(UPLOAD_PATH);
            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            ImageIO.write(bigImageOut, "png", new File(UPLOAD_PATH, file.getOriginalFilename()));
            ImageIO.write(smallImageOut, "png", new File(UPLOAD_PATH + "/preview", file.getOriginalFilename()));
            String fileName = file.getOriginalFilename();
            this.picture.setName(file.getOriginalFilename());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private BufferedImage resizePicture(BufferedImage uploadedImage, int newWidth, int newHeight) {
        BufferedImage imageOut;
        if (uploadedImage.getWidth() > uploadedImage.getHeight()) {
            imageOut = Scalr.resize(uploadedImage, Scalr.Method.SPEED, Scalr.Mode.FIT_TO_WIDTH, newWidth);
        } else {
//            double ratio = (double)uploadedImage.getHeight() / newHeight;
//            int outWidth = (int)Math.round((double)uploadedImage.getWidth() / ratio);
            imageOut = Scalr.resize(uploadedImage, Scalr.Method.SPEED, Scalr.Mode.FIT_TO_HEIGHT, newHeight);
        }
        return imageOut;
    }

    public Picture getPicture() {
        return picture;
    }

//    @Autowired
//    public void setPicture(Picture picture) {
//        this.picture = picture;
//    }

}
