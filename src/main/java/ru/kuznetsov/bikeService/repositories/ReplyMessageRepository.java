package ru.kuznetsov.bikeService.repositories;

import org.springframework.stereotype.Repository;
import ru.kuznetsov.bikeService.models.ReplyMessage;
import ru.kuznetsov.bikeService.repositories.abstracts.CommonRepository;

@Repository
public interface ReplyMessageRepository extends CommonRepository<ReplyMessage> {
}
