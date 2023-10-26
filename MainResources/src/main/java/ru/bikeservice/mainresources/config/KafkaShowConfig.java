package ru.bikeservice.mainresources.config;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.bikeservice.mainresources.models.dto.kafka.EntityKafkaTransfer;
import ru.bikeservice.mainresources.models.dto.kafka.ShowableGetter;

@EnableKafka
@Configuration
public class KafkaShowConfig extends KafkaConfig {
    @Bean
    public ProducerFactory<String, EntityKafkaTransfer> showProducerFactory() {
        return new DefaultKafkaProducerFactory<>(getProducerProps(),
                new StringSerializer(),
                new JsonSerializer<>());
    }

    @Bean
    public KafkaTemplate<String, EntityKafkaTransfer> showKafkaTemplate(
            ProducerFactory<String, EntityKafkaTransfer> pf,
            ConcurrentKafkaListenerContainerFactory<String, ShowableGetter> factory) {
        KafkaTemplate<String, EntityKafkaTransfer> kafkaTemplate = new KafkaTemplate<>(pf);
        factory.getContainerProperties().setMissingTopicsFatal(false);
        factory.setReplyTemplate(kafkaTemplate);
        return kafkaTemplate;
    }

    @Bean
    public ConsumerFactory<String, ShowableGetter> showConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(getConsumerProps(),
                new StringDeserializer(),
                new JsonDeserializer<>(ShowableGetter.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ShowableGetter>
    showListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ShowableGetter> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(showConsumerFactory());
        return factory;
    }
}
