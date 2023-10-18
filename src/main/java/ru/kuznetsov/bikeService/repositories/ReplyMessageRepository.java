package ru.kuznetsov.bikeService.repositories;

import org.springframework.stereotype.Repository;
import ru.bikeservice.mainresources.models.ReplyMessage;
import ru.bikeservice.mainresources.repositories.abstracts.CommonRepository;

@Repository
public interface ReplyMessageRepository extends CommonRepository<ReplyMessage> {
}
