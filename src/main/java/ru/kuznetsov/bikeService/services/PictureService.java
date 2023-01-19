package ru.kuznetsov.bikeService.services;

import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.Picture;
import ru.kuznetsov.bikeService.repositories.PictureRepository;
import ru.kuznetsov.bikeService.services.abstracts.AbstractService;

@Service
public class PictureService extends AbstractService<Picture, PictureRepository> {
    public PictureService(PictureRepository repository) {
        super(repository);
    }
}
