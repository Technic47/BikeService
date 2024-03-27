//package ru.bikeservice.mainresources.config.kafkaConfig;
//
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.apache.kafka.common.serialization.StringSerializer;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaProducerFactory;
//import org.springframework.kafka.core.ProducerFactory;
//import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
//import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
//import org.springframework.kafka.support.serializer.JsonDeserializer;
//import org.springframework.kafka.support.serializer.JsonSerializer;
//import ru.bikeservice.mainresources.models.dto.kafka.EntityKafkaTransfer;
//
//@Configuration
//public class KafkaCreatorConfig extends KafkaConfig {
//    @Value("${kafka.reply.topic.creator}")
//    private String creatorReplyTopic;
//
//    @Bean
//    public ReplyingKafkaTemplate<String, EntityKafkaTransfer, EntityKafkaTransfer> creatorReplyingKafkaTemplate(
//            ProducerFactory<String, EntityKafkaTransfer> pf,
//            ConcurrentKafkaListenerContainerFactory<String, EntityKafkaTransfer> factory) {
//        ConcurrentMessageListenerContainer<String, EntityKafkaTransfer> replyContainer =
//                factory.createContainer(creatorReplyTopic);
//        replyContainer.getContainerProperties().setMissingTopicsFatal(false);
//        replyContainer.getContainerProperties().setGroupId(kafkaGroupId);
//        return new ReplyingKafkaTemplate<>(pf, replyContainer);
//    }
//
//    @Bean
//    public ProducerFactory<String, EntityKafkaTransfer> creatorProducerFactory() {
//        return new DefaultKafkaProducerFactory<>(getProducerProps(),
//                new StringSerializer(),
//                new JsonSerializer<>());
//    }
//
//    @Bean
//    public ConsumerFactory<String, EntityKafkaTransfer> creatorConsumerFactory() {
//        return new DefaultKafkaConsumerFactory<>(getConsumerProps(),
//                new StringDeserializer(),
//                new JsonDeserializer<>());
//    }
//
////    @Bean
////    public ConcurrentKafkaListenerContainerFactory<String, AbstractShowableEntity>
////    creatorKafkaListenerContainerFactory() {
////        ConcurrentKafkaListenerContainerFactory<String, AbstractShowableEntity> factory =
////                new ConcurrentKafkaListenerContainerFactory<>();
////        factory.setConsumerFactory(creatorConsumerFactory());
////        return factory;
////    }
//}
