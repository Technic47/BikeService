package ru.kuznetsov.bikeService.models;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Picture {
    private int id;
    private String preview;
    private String big;

    public static String UPLOAD_DIRECTORY_BIG = "src/main/webapp/IMG";
    public static String UPLOAD_DIRECTORY_PREVIEW = "src/main/webapp/IMG/preview";

    public Picture(MultipartFile file){
        this.managePicture(file);
    }

    private void managePicture(MultipartFile file) {
        try {
            BufferedImage uploadedImage = ImageIO.read(file.getResource().getFile());
            BufferedImage bigImageOut = convertPicture(uploadedImage, 800, 600);
            BufferedImage smallImageOut = convertPicture(uploadedImage, 64, 64);

            String bigPath = UPLOAD_DIRECTORY_BIG + "/" + file.getOriginalFilename() + uploadedImage.getType();
            ImageIO.write(bigImageOut, "png", new File(bigPath));
            String previewPath = UPLOAD_DIRECTORY_PREVIEW + "/" + file.getOriginalFilename() + uploadedImage.getType();
            ImageIO.write(smallImageOut, "png", new File(previewPath));
            this.setBig(bigPath);
            this.setPreview(previewPath);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private BufferedImage convertPicture(BufferedImage uploadedImage, int newWidth, int newHeigth) {
        BufferedImage imageOut;
        if (uploadedImage.getWidth() > uploadedImage.getHeight()) {
            imageOut = new BufferedImage(newWidth, -1, uploadedImage.getType());
        } else {
            imageOut = new BufferedImage(-1, newHeigth, uploadedImage.getType());
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
