package ru.bikeservice.mainresources.config;

import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import ru.bikeservice.mainresources.models.dto.kafka.SearchKafkaDTO;

@Configuration
public class KafkaSearchConfig extends KafkaConfig {
    @Bean
    public ConsumerFactory<String, SearchKafkaDTO> searchConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(getConsumerProps(),
                new StringDeserializer(),
                new JsonDeserializer<>(SearchKafkaDTO.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SearchKafkaDTO>
    searchListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, SearchKafkaDTO> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(searchConsumerFactory());
        return factory;
    }
}
