package ru.kuznetsov.bikeService.repositories;

import org.springframework.stereotype.Repository;
import ru.bikeservice.mainresources.repositories.abstracts.CommonRepository;
import ru.kuznetsov.bikeService.models.pictures.Picture;

@Repository
public interface PictureRepository extends CommonRepository<Picture> {
}
