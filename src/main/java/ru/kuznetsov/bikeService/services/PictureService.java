package ru.kuznetsov.bikeService.services;

import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.pictures.Picture;
import ru.kuznetsov.bikeService.repositories.PictureRepository;
import ru.kuznetsov.bikeService.services.abstracts.AbstractService;

@Service
public class PictureService extends AbstractService<Picture, PictureRepository> {
    public PictureService(PictureRepository repository) {
        super(repository);
    }

    @Override
    public void update(Long id, Picture newPicture) {
        Picture toRepo = this.show(id);
        toRepo.setName(newPicture.getName());
        this.save(toRepo);
    }
}
