package ru.bikeservice.mainresources.models.showable;

import ru.bikeservice.mainresources.models.dto.kafka.EntityKafkaTransfer;

public interface ConvertableFromDTO {
    void convertFromDTO(EntityKafkaTransfer dto);
}
