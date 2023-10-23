package ru.kuznetsov.bikeService.repositories;

import org.springframework.stereotype.Repository;
import ru.bikeservice.mainresources.repositories.abstracts.CommonRepository;
import ru.kuznetsov.bikeService.models.ReplyMessage;

@Repository
public interface ReplyMessageRepository extends CommonRepository<ReplyMessage> {
}
