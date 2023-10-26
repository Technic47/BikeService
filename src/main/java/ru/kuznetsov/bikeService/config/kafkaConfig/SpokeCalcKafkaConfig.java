package ru.kuznetsov.bikeService.config.kafkaConfig;

import org.apache.kafka.common.serialization.DoubleDeserializer;
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
import org.springframework.kafka.support.serializer.JsonSerializer;
import ru.bikeservice.mainresources.config.KafkaConfig;

import java.util.Map;

@Configuration
public class SpokeCalcKafkaConfig extends KafkaConfig {
    @Value("${kafka.reply.topic.spokeCalc}")
    private String replyTopicSpokeCalc;

    @Bean
    public ReplyingKafkaTemplate<String, Map<String, Double>, Double> spokeCalcReplyingKafkaTemplate(
            ProducerFactory<String, Map<String, Double>> pf,
            ConcurrentKafkaListenerContainerFactory<String, Double> factory) {
        ConcurrentMessageListenerContainer<String, Double> replyContainer = factory.createContainer(replyTopicSpokeCalc);
        replyContainer.getContainerProperties().setMissingTopicsFatal(false);
        replyContainer.getContainerProperties().setGroupId(kafkaGroupId);
        return new ReplyingKafkaTemplate<>(pf, replyContainer);
    }

    @Bean
    public ProducerFactory<String, Map<String, Double>> spokeCalcProducerFactory() {
        return new DefaultKafkaProducerFactory<>(getProducerProps(),
                new StringSerializer(),
                new JsonSerializer<>());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Double>
    spokeCalkKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Double> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(spokeCalkConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, Double> spokeCalkConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(getConsumerProps(),
                new StringDeserializer(),
                new DoubleDeserializer());
    }
}
