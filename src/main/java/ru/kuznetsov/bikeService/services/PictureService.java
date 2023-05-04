package ru.kuznetsov.bikeService.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.kuznetsov.bikeService.models.pictures.Picture;
import ru.kuznetsov.bikeService.models.pictures.PictureWork;
import ru.kuznetsov.bikeService.repositories.PictureRepository;
import ru.kuznetsov.bikeService.services.abstracts.AbstractService;

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

    @Autowired
    public void setPictureWork(PictureWork pictureWork) {
        this.pictureWork = pictureWork;
        this.pictureWork.setPicture(new Picture());
    }
}
