package ru.bikeservice.mainresources.repositories.abstracts;

import org.springframework.data.repository.NoRepositoryBean;
import ru.bikeservice.mainresources.models.abstracts.AbstractUsableEntity;

import java.util.List;

@NoRepositoryBean
public interface AbstractUsableEntityRepository<E extends AbstractUsableEntity>
        extends AbstractShowableEntityRepository<E> {
    List<E> findByManufacturer(Long manufacturerId);
    List<E> findByManufacturerAndCreatorOrIsShared(Long manufacturerId, Long creatorId, boolean status);
    List<E> findByModel(String model);
    List<E> findByModelAndCreatorOrIsShared(String model, Long creatorId, boolean status);
}
