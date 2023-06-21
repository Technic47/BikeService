package ru.kuznetsov.bikeService.services;

import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.ReplyMessage;
import ru.kuznetsov.bikeService.repositories.ReplyMessageRepository;
import ru.kuznetsov.bikeService.services.abstracts.AbstractService;

@Service
public class ReplyMessageService extends AbstractService<ReplyMessage, ReplyMessageRepository> {
    protected ReplyMessageService(ReplyMessageRepository repository) {
        super(repository);
    }
}
