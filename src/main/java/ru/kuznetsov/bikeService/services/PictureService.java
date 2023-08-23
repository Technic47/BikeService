package ru.kuznetsov.bikeService.services;

import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.kuznetsov.bikeService.controllers.abstracts.AbstractController;
import ru.kuznetsov.bikeService.models.pictures.Picture;
import ru.kuznetsov.bikeService.repositories.PictureRepository;
import ru.kuznetsov.bikeService.services.abstracts.AbstractService;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

import static ru.kuznetsov.bikeService.config.SpringConfig.UPLOAD_PATH;

@Service
public class PictureService extends AbstractService<Picture, PictureRepository> {
    private final static Integer PICTURE_WIDTH = 400;
    private final static Integer PICTURE_HEIGHT = 300;
    private final static Integer PREVIEW_WIDTH = 64;
    private final static Integer PREVIEW_HEIGHT = 64;
    private final ExecutorService additionalExecutor;


    public PictureService(PictureRepository repository,
                          @Qualifier("AdditionExecutor") ExecutorService additionalExecutor) {
        super(repository);
        this.additionalExecutor = additionalExecutor;
    }

    @Override
    public Picture save(Picture entity) {
        return super.save(entity);
    }

    /**
     * 2 files created after conversion of MultipartFile and new record is saved to DB.
     *
     * @param file MultipartFile with picture
     * @return Picture object after saving.
     */
    public Picture save(MultipartFile file) {
        Runnable managePicture = () -> this.managePicture(file);
        mainExecutor.submit(managePicture);
        return super.save(new Picture(file.getOriginalFilename()));
    }

    private void managePicture(MultipartFile file) {
        try {
            this.dirCheck();
            BufferedImage uploadedImage = ImageIO.read(file.getInputStream());
            Callable<BufferedImage> resizeBig = () -> resizePicture(uploadedImage, PICTURE_WIDTH, PICTURE_HEIGHT);
            Callable<BufferedImage> resizeSmall = () -> resizePicture(uploadedImage, PREVIEW_WIDTH, PREVIEW_HEIGHT);
            BufferedImage bigImageOut = additionalExecutor.submit(resizeBig).get();
            BufferedImage smallImageOut = additionalExecutor.submit(resizeSmall).get();
            Runnable writeBig = () -> {
                try {
                    this.writeFile(bigImageOut, new File(UPLOAD_PATH, file.getOriginalFilename()));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            };
            Runnable writeSmall = () -> {
                try {
                    this.writeFile(smallImageOut, new File(UPLOAD_PATH + "/preview", file.getOriginalFilename()));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            };
            additionalExecutor.submit(writeBig);
            additionalExecutor.submit(writeSmall);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

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

    @Override
    public Picture update(Long id, Picture newPicture) {
        Picture toRepo = this.getById(id);
        toRepo.setName(newPicture.getName());
        return this.save(toRepo);
    }

    @Override
    public Picture update(Picture item, Picture updateItem) {
        return null;
    }

    /**
     * Delete picture with Long id from DB.
     * If record is present file is deleted from UPLOAD_PATH and preview.
     *
     * @param id picture id.
     */
    @Override
    public void delete(Long id) {
        try {
            Optional<Picture> entity = this.repository.findById(id);
            if (entity.isPresent()) {
                String imgPath = UPLOAD_PATH + "/" + entity.get().getName();
                File file = new File(imgPath);
                imgPath = UPLOAD_PATH + "/preview/" + entity.get().getName();
                File previewFile = new File(imgPath);
                file.delete();
                previewFile.delete();
                super.delete(id);
            } else {
                throw new FileNotFoundException("Picture not found.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            AbstractController.logger.warn("Picture with id = " + id + " not found. Can`t delete!");
        }
    }
}
