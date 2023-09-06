package ru.kuznetsov.bikeService.repositories.abstracts;

import org.springframework.data.repository.NoRepositoryBean;
import ru.kuznetsov.bikeService.models.abstracts.AbstractUsableEntity;

import java.util.List;

@NoRepositoryBean
public interface AbstractUsableEntityRepository<E extends AbstractUsableEntity>
        extends AbstractShowableEntityRepository<E> {
    List<E> findByManufacturer(Long manufacturerId);
    List<E> findByManufacturerAndCreatorOrIsShared(Long manufacturerId, Long creatorId, boolean status);
    List<E> findByModelAndCreatorOrIsShared(String model, Long creatorId, boolean status);
}
