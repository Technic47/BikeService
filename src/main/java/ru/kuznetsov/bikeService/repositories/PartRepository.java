package ru.kuznetsov.bikeService.repositories;

import org.springframework.stereotype.Repository;
import ru.kuznetsov.bikeService.models.servicable.Part;
import ru.kuznetsov.bikeService.repositories.abstracts.CommonRepository;

@Repository
public interface PartRepository extends CommonRepository<Part> {
}
