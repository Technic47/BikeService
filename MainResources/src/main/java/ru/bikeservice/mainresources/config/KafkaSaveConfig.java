package ru.bikeservice.mainresources.config;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.bikeservice.mainresources.models.dto.kafka.EntityKafkaTransfer;

@Configuration
public class KafkaSaveConfig extends KafkaConfig {

//    @Bean
//    public ProducerFactory<String, EntityKafkaTransfer> producerFactory() {
//        return new DefaultKafkaProducerFactory<>(getProducerProps(),
//                new StringSerializer(),
//                new JsonSerializer<>());
//    }

    @Bean
    public KafkaTemplate<String, EntityKafkaTransfer> replyTemplate(
            ProducerFactory<String, EntityKafkaTransfer> pf,
            ConcurrentKafkaListenerContainerFactory<String, EntityKafkaTransfer> factory) {
        KafkaTemplate<String, EntityKafkaTransfer> kafkaTemplate = new KafkaTemplate<>(pf);
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
    entityListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, EntityKafkaTransfer> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(creatorConsumerFactory());
        return factory;
    }
}
