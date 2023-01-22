package ru.kuznetsov.bikeService.services;

import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.Picture;
import ru.kuznetsov.bikeService.repositories.PictureRepository;
import ru.kuznetsov.bikeService.services.abstracts.AbstractShowableService;

@Service
public class PictureShowableService extends AbstractShowableService<Picture, PictureRepository> {
    public PictureShowableService(PictureRepository repository) {
        super(repository);
    }
}
