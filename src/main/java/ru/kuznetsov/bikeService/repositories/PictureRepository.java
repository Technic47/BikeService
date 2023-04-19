package ru.kuznetsov.bikeService.repositories;

import org.springframework.stereotype.Repository;
import ru.kuznetsov.bikeService.models.pictures.Picture;
import ru.kuznetsov.bikeService.repositories.abstracts.CommonRepository;

@Repository
public interface PictureRepository extends CommonRepository<Picture> {
}
