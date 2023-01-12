package ru.kuznetsov.bikeService.models;

import org.imgscalr.Scalr;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

import static ru.kuznetsov.bikeService.config.SpringConfig.UPLOAD_DIRECTORY_BIG;
import static ru.kuznetsov.bikeService.config.SpringConfig.UPLOAD_DIRECTORY_PREVIEW;

public class Picture {
    private int id;
    private String preview;
    private String big;



    public void managePicture(MultipartFile file) {
        try {
            BufferedImage uploadedImage = ImageIO.read(file.getInputStream());
            BufferedImage bigImageOut = resizePicture(uploadedImage, 800, 600);
            BufferedImage smallImageOut = resizePicture(uploadedImage, 64, 64);

            String bigPath = UPLOAD_DIRECTORY_BIG + "/" + file.getOriginalFilename();
            String previewPath = UPLOAD_DIRECTORY_PREVIEW + "/" + file.getOriginalFilename();
            ImageIO.write(bigImageOut, "png", new File(UPLOAD_DIRECTORY_BIG, file.getOriginalFilename()));
            ImageIO.write(smallImageOut, "png", new File(UPLOAD_DIRECTORY_PREVIEW, file.getOriginalFilename()));

            this.setBig(bigPath);
            this.setPreview(previewPath);
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getBig() {
        return big;
    }

    public void setBig(String big) {
        this.big = big;
    }
}
