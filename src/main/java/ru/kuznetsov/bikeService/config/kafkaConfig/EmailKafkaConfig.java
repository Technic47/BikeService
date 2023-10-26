package ru.kuznetsov.bikeService.config.kafkaConfig;

import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class EmailKafkaConfig extends KafkaConfig {
    @Bean
    public ProducerFactory<String, byte[]> emailProducerFactory() {
        return new DefaultKafkaProducerFactory<>(getProducerProps(),
                new StringSerializer(),
                new ByteArraySerializer());
    }

    @Bean
    public KafkaTemplate<String, byte[]> emailKafkaTemplate() {
        return new KafkaTemplate<>(emailProducerFactory());
    }
}
