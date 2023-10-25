package ru.kuznetsov.bikeService.config.kafkaConfig;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.bikeservice.mainresources.config.KafkaConfig;
import ru.bikeservice.mainresources.models.dto.kafka.EntityKafkaTransfer;
import ru.bikeservice.mainresources.models.dto.kafka.IndexKafkaDTO;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class IndexProducerConfig extends KafkaConfig {
    @Value("${kafka.reply.topic.index}")
    private String replyIndex;

    @Bean
    public ReplyingKafkaTemplate<String, IndexKafkaDTO, EntityKafkaTransfer[]>
    indexReplyingKafkaTemplate(
            ProducerFactory<String, IndexKafkaDTO> pf,
            ConcurrentKafkaListenerContainerFactory<String, EntityKafkaTransfer[]> factory) {
        ConcurrentMessageListenerContainer<String, EntityKafkaTransfer[]> replyContainer =
                factory.createContainer(replyIndex);
        replyContainer.getContainerProperties().setMissingTopicsFatal(false);
        replyContainer.getContainerProperties().setGroupId(kafkaGroupId);
        return new ReplyingKafkaTemplate<>(pf, replyContainer);
    }

    @Bean
    public ProducerFactory<String, IndexKafkaDTO> indexProducerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, EntityKafkaTransfer[]>
    indexKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, EntityKafkaTransfer[]> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(indexConsumerFactory());
        factory.setBatchListener(true);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, EntityKafkaTransfer[]> indexConsumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return new DefaultKafkaConsumerFactory<>(props);
    }
}
