package ru.kuznetsov.bikeService.repositories;

import org.springframework.stereotype.Repository;
import ru.kuznetsov.bikeService.models.service.Tool;
import ru.kuznetsov.bikeService.repositories.abstracts.CommonRepository;

@Repository
public interface ToolRepository extends CommonRepository<Tool> {
}