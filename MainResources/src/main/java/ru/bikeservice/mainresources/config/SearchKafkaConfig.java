package ru.bikeservice.mainresources.config;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.bikeservice.mainresources.models.dto.kafka.ShowableGetter;

import java.util.HashMap;
import java.util.Map;

public class SearchKafkaConfig extends KafkaConfig{
    @Bean
    public ConsumerFactory<String, ShowableGetter> consumerFactory() {
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
        factory.setConsumerFactory(consumerFactory());
        factory.setBatchListener(false);
        return factory;
    }
}
