//package ru.bikeservice.mainresources.config;
//
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.springframework.kafka.support.serializer.JsonDeserializer;
//import ru.bikeservice.mainresources.models.dto.kafka.EntityKafkaTransfer;
//
//@Configuration
//public class KafkaDeleteConfig extends KafkaConfig {
//    @Bean
//    public ConsumerFactory<String, EntityKafkaTransfer> deleteConsumerFactory() {
//        return new DefaultKafkaConsumerFactory<>(getConsumerProps(),
//                new StringDeserializer(),
//                new JsonDeserializer<>(EntityKafkaTransfer.class));
//    }
//
//    @Bean
//    public ConcurrentKafkaListenerContainerFactory<String, EntityKafkaTransfer>
//    kafkaListenerContainerFactory() {
//        ConcurrentKafkaListenerContainerFactory<String, EntityKafkaTransfer> factory =
//                new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(deleteConsumerFactory());
//        return factory;
//    }
//}
