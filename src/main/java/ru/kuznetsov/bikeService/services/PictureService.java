package ru.kuznetsov.bikeService.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.kuznetsov.bikeService.models.pictures.Picture;
import ru.kuznetsov.bikeService.models.pictures.PictureWork;
import ru.kuznetsov.bikeService.repositories.PictureRepository;
import ru.kuznetsov.bikeService.services.abstracts.AbstractService;

import java.io.File;
import java.util.Optional;

import static ru.kuznetsov.bikeService.config.SpringConfig.UPLOAD_PATH;

@Service
public class PictureService extends AbstractService<Picture, PictureRepository> {
    private PictureWork pictureWork;


    public PictureService(PictureRepository repository) {
        super(repository);
    }

    @Override
    public Picture save(Picture entity) {
        return super.save(entity);
    }

    public Picture save(MultipartFile file) {
        this.pictureWork.managePicture(file);
        return super.save(pictureWork.getPicture());
    }

    @Override
    public void update(Long id, Picture newPicture) {
        Picture toRepo = this.show(id);
        toRepo.setName(newPicture.getName());
        this.save(toRepo);
    }


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
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        super.delete(id);
    }

    @Autowired
    public void setPictureWork(PictureWork pictureWork) {
        this.pictureWork = pictureWork;
        this.pictureWork.setPicture(new Picture());
    }
}
