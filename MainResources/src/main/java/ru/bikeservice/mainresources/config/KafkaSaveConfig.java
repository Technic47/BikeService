package ru.bikeservice.mainresources.config;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.bikeservice.mainresources.models.dto.kafka.EntityKafkaTransfer;
import ru.bikeservice.mainresources.models.showable.Showable;

@Configuration
public class KafkaSaveConfig extends KafkaConfig {

    @Bean
    public ProducerFactory<String, Showable> producerFactory() {
        return new DefaultKafkaProducerFactory<>(getProducerProps(),
                new StringSerializer(),
                new JsonSerializer<>());
    }

    @Bean
    public KafkaTemplate<String, Showable> replyTemplate(
            ProducerFactory<String, Showable> pf,
            ConcurrentKafkaListenerContainerFactory<String, EntityKafkaTransfer> factory) {
        KafkaTemplate<String, Showable> kafkaTemplate = new KafkaTemplate<>(pf);
        factory.getContainerProperties().setMissingTopicsFatal(false);
        factory.setReplyTemplate(kafkaTemplate);
        return kafkaTemplate;
    }

    @Bean
    public ConsumerFactory<String, EntityKafkaTransfer> creatorConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(getConsumerProps(),
                new StringDeserializer(),
                new JsonDeserializer<>(EntityKafkaTransfer.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, EntityKafkaTransfer>
    saveListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, EntityKafkaTransfer> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(creatorConsumerFactory());
        return factory;
    }
}
