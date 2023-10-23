package ru.bikeservice.mainresources.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.bikeservice.mainresources.models.abstracts.AbstractShowableEntity;
import ru.bikeservice.mainresources.models.dto.kafka.ShowableGetter;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaServiceConfig extends KafkaConfig {
    @Bean
    public ProducerFactory<String, AbstractShowableEntity> multiTypeProducerFactory() {
        Map<String, Object> producerProps = new HashMap<>();
        producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
//        producerProps.put(JsonSerializer.TYPE_MAPPINGS,
//                "Document:ru/bikeservice/mainresources/models/showable/Document," +
//                        "Fastener:ru/bikeservice/mainresources/models/showable/Fastener," +
//                        "Manufacturer:ru/bikeservice/mainresources/models/showable/Manufacturer," +
//                        "Consumable:ru/bikeservice/mainresources/models/showable/Consumable," +
//                        "Tool:ru/bikeservice/mainresources/models/showable/Tool," +
//                        "Part:ru/bikeservice/mainresources/models/showable/Part," +
//                        "Bike:ru/bikeservice/mainresources/models/showable/Bike"
//        );
        return new DefaultKafkaProducerFactory<>(producerProps);
    }

    @Bean
    public KafkaTemplate<String, AbstractShowableEntity> multiTypeKafkaTemplate(
            ProducerFactory<String, AbstractShowableEntity> pf,
            ConcurrentKafkaListenerContainerFactory<String, ShowableGetter> factory) {
        KafkaTemplate<String, AbstractShowableEntity> kafkaTemplate = new KafkaTemplate<>(pf);
        factory.getContainerProperties().setMissingTopicsFatal(false);
        factory.setReplyTemplate(kafkaTemplate);
        return kafkaTemplate;
    }

    @Bean
    public ConsumerFactory<String, ShowableGetter> showConsumerFactory() {
        Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId);
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
//        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return new DefaultKafkaConsumerFactory<>(consumerProps,
                new StringDeserializer(),
                new JsonDeserializer<>(ShowableGetter.class, false));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ShowableGetter>
    kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ShowableGetter> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(showConsumerFactory());
        factory.setBatchListener(false);
        return factory;
    }
}
