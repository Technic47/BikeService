package ru.kuznetsov.bikeService.repositories;

import org.springframework.stereotype.Repository;
import ru.kuznetsov.bikeService.models.showable.Fastener;
import ru.kuznetsov.bikeService.repositories.abstracts.CommonRepository;

@Repository
public interface FastenerRepository extends CommonRepository<Fastener> {
}
