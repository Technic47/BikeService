package ru.bikeservice.mainresources.repositories;

import org.springframework.stereotype.Repository;
import ru.bikeservice.mainresources.models.pictures.Picture;
import ru.bikeservice.mainresources.repositories.abstracts.CommonRepository;

@Repository
public interface PictureRepository extends CommonRepository<Picture> {
}
