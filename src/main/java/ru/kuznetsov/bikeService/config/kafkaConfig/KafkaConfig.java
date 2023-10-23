package ru.kuznetsov.bikeService.config.kafkaConfig;

import org.springframework.beans.factory.annotation.Value;

public abstract class KafkaConfig {
    @Value(value = "${spring.kafka.bootstrap-servers}")
    protected String bootstrapAddress;
    @Value("${kafka.group.id}")
    protected String kafkaGroupId;
}
