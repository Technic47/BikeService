package ru.kuznetsov.bikeService.services;

import org.springframework.stereotype.Service;
import ru.bikeservice.mainresources.models.ReplyMessage;
import ru.bikeservice.mainresources.services.abstracts.AbstractService;
import ru.kuznetsov.bikeService.repositories.ReplyMessageRepository;

@Service
public class ReplyMessageService extends AbstractService<ReplyMessage, ReplyMessageRepository> {
    protected ReplyMessageService(ReplyMessageRepository repository) {
        super(repository);
    }

    @Override
    public ReplyMessage update(Long id, ReplyMessage updateItem) {
        return null;
    }

    @Override
    public ReplyMessage update(ReplyMessage item, ReplyMessage updateItem) {
        return null;
    }
}
