//package ru.bikeservice.mainresources.config.kafkaConfig;
//
//import org.apache.kafka.common.serialization.StringSerializer;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
//import org.springframework.kafka.core.DefaultKafkaProducerFactory;
//import org.springframework.kafka.core.ProducerFactory;
//import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
//import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
//import org.springframework.kafka.support.serializer.JsonSerializer;
//import ru.bikeservice.mainresources.models.dto.kafka.EntityKafkaTransfer;
//import ru.bikeservice.mainresources.models.dto.kafka.SearchKafkaDTO;
//
//@Configuration
//public class SearchKafkaProducerConfig extends KafkaConfig {
//    @Value("${kafka.reply.topic.search}")
//    private String searchReply;
//
//    @Bean
//    public ReplyingKafkaTemplate<String, SearchKafkaDTO, EntityKafkaTransfer[]>
//    searchReplyingKafkaTemplate(
//            ProducerFactory<String, SearchKafkaDTO> pf,
//            ConcurrentKafkaListenerContainerFactory<String, EntityKafkaTransfer[]> factory) {
//        ConcurrentMessageListenerContainer<String, EntityKafkaTransfer[]> replyContainer =
//                factory.createContainer(searchReply);
//        replyContainer.getContainerProperties().setMissingTopicsFatal(false);
//        replyContainer.getContainerProperties().setGroupId(kafkaGroupId);
//        return new ReplyingKafkaTemplate<>(pf, replyContainer);
//    }
//
//    @Bean
//    public ProducerFactory<String, SearchKafkaDTO> searchProducerFactory() {
//        return new DefaultKafkaProducerFactory<>(getProducerProps(),
//                new StringSerializer(),
//                new JsonSerializer<>());
//    }
//
////    @Bean
////    public ConsumerFactory<String, EntityKafkaTransfer[]> searchConsumerFactory() {
////        return new DefaultKafkaConsumerFactory<>(getConsumerProps(),
////                new StringDeserializer(),
////                new JsonDeserializer<>());
////    }
//}
