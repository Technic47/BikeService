package ru.bikeservice.mainresources.repositories.modelRepositories;

import org.springframework.data.domain.Example;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ru.bikeservice.mainresources.models.showable.Manufacturer;
import ru.bikeservice.mainresources.repositories.abstracts.AbstractShowableEntityRepository;

@Repository
public interface ManufacturerRepository extends AbstractShowableEntityRepository<Manufacturer> {

    @Override
    <S extends Manufacturer> boolean exists(@NonNull Example<S> example);
}
