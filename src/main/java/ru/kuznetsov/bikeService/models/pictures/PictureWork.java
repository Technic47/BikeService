//package ru.kuznetsov.bikeService.models.pictures;
//
//import org.imgscalr.Scalr;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Component;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.util.concurrent.Callable;
//import java.util.concurrent.ExecutorService;
//
//import static ru.kuznetsov.bikeService.config.SpringConfig.UPLOAD_PATH;
//
//@Component
//public class PictureWork {
//    private final static Integer PICTURE_WIDTH = 400;
//    private final static Integer PICTURE_HEIGHT = 300;
//    private final static Integer PREVIEW_WIDTH = 64;
//    private final static Integer PREVIEW_HEIGHT = 64;
//
//    private ExecutorService additionalExecutor;
//
//    public PictureWork() {
//    }
//
//    /**
//     * Processes MultipartFile. Take out picture, resize it and save to UPLOAD_PATH.
//     *
//     * @param file file with picture to save.
//     */
//    public Picture managePicture(MultipartFile file) {
//        try {
//            this.dirCheck();
//            BufferedImage uploadedImage = ImageIO.read(file.getInputStream());
//            Callable<BufferedImage> resizeBig = () -> resizePicture(uploadedImage, PICTURE_WIDTH, PICTURE_HEIGHT);
//            Callable<BufferedImage> resizeSmall = () -> resizePicture(uploadedImage, PREVIEW_WIDTH, PREVIEW_HEIGHT);
//            BufferedImage bigImageOut = additionalExecutor.submit(resizeBig).get();
//            BufferedImage smallImageOut = additionalExecutor.submit(resizeSmall).get();
//
//            Runnable writeBig = () -> {
//                try {
//                    this.writeFile(bigImageOut, new File(UPLOAD_PATH, file.getOriginalFilename()));
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            };
//            Runnable writeSmall = () -> {
//                try {
//                    this.writeFile(smallImageOut, new File(UPLOAD_PATH + "/preview", file.getOriginalFilename()));
//                } catch (Exception e) {
//                    throw new RuntimeException(e);
//                }
//            };
//            additionalExecutor.submit(writeBig);
//            additionalExecutor.submit(writeSmall);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//        return new Picture(file.getOriginalFilename());
//    }
//
//    /**
//     * Convert image to specified width and height.
//     * Checks if image is tall or wide and fits it to size.
//     *
//     * @param uploadedImage image for conversion.
//     * @param newWidth      target width.
//     * @param newHeight     target height.
//     * @return - converted image.
//     */
//    private BufferedImage resizePicture(
//            BufferedImage uploadedImage, int newWidth, int newHeight) {
//        BufferedImage imageOut;
//        if (uploadedImage.getWidth() > uploadedImage.getHeight()) {
//            imageOut = Scalr.resize(uploadedImage,
//                    Scalr.Method.SPEED, Scalr.Mode.FIT_TO_WIDTH, newWidth);
//        } else {
//            imageOut = Scalr.resize(uploadedImage,
//                    Scalr.Method.SPEED, Scalr.Mode.FIT_TO_HEIGHT, newHeight);
//        }
//        return imageOut;
//    }
//
//    private void dirCheck() {
//        File uploadDir = new File(UPLOAD_PATH);
//        if (!uploadDir.exists()) {
//            uploadDir.mkdir();
//        }
//    }
//
//    private void writeFile(BufferedImage image, File file) throws Exception {
//        try {
//            ImageIO.write(image, "png", file);
//        } catch (Exception e) {
//            throw new Exception(e.getMessage());
//        }
//    }
//
//    @Autowired
//    @Qualifier("AdditionExecutor")
//    public void setAdditionalExecutor(ExecutorService additionalExecutor) {
//        this.additionalExecutor = additionalExecutor;
//    }
//}
