package ru.kuznetsov.bikeService.config.kafkaConfig;

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
import ru.bikeservice.mainresources.models.dto.kafka.EntityKafkaTransfer;
import ru.bikeservice.mainresources.models.dto.kafka.ShowableGetter;

@Configuration
public class ShowProducerConfig extends KafkaConfig {
    @Value("${kafka.reply.topic.show}")
    private String replyShow;

    @Bean
    public ReplyingKafkaTemplate<String, ShowableGetter, EntityKafkaTransfer>
    showReplyingKafkaTemplate(
            ProducerFactory<String, ShowableGetter> pf,
            ConcurrentKafkaListenerContainerFactory<String, EntityKafkaTransfer> factory) {
        ConcurrentMessageListenerContainer<String, EntityKafkaTransfer> replyContainer =
                factory.createContainer(replyShow);
        replyContainer.getContainerProperties().setMissingTopicsFatal(false);
        replyContainer.getContainerProperties().setGroupId(kafkaGroupId);
        return new ReplyingKafkaTemplate<>(pf, replyContainer);
    }

    @Bean
    public ProducerFactory<String, ShowableGetter> showProducerFactory() {
        return new DefaultKafkaProducerFactory<>(getProducerProps(),
                new StringSerializer(),
                new JsonSerializer<>());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, EntityKafkaTransfer>
    mainResourcesKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, EntityKafkaTransfer> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(showConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, EntityKafkaTransfer> showConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(getConsumerProps(),
                new StringDeserializer(),
                new JsonDeserializer<>());
    }
}
