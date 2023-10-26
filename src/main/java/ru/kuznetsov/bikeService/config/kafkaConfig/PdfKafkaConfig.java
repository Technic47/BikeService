package ru.kuznetsov.bikeService.config.kafkaConfig;

import org.apache.kafka.common.serialization.ByteArrayDeserializer;
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
import ru.bikeservice.mainresources.models.dto.PdfEntityDto;

@Configuration
public class PdfKafkaConfig extends KafkaConfig {
    @Value("${kafka.reply.topic.pdf}")
    private String replyTopicPdf;

    @Bean
    public ReplyingKafkaTemplate<String, PdfEntityDto, byte[]>
    pdfReplyingKafkaTemplate(
            ProducerFactory<String, PdfEntityDto> pf,
            ConcurrentKafkaListenerContainerFactory<String, byte[]> factory) {
        ConcurrentMessageListenerContainer<String, byte[]> replyContainer =
                factory.createContainer(replyTopicPdf);
        replyContainer.getContainerProperties().setMissingTopicsFatal(false);
        replyContainer.getContainerProperties().setGroupId(kafkaGroupId);
        return new ReplyingKafkaTemplate<>(pf, replyContainer);
    }

    @Bean
    public ProducerFactory<String, PdfEntityDto> pdfProducerFactory() {
        return new DefaultKafkaProducerFactory<>(getProducerProps(),
                new StringSerializer(),
                new JsonSerializer<>());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, byte[]>
    pdfKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, byte[]> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(pdfConsumerFactory());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, byte[]> pdfConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(getConsumerProps(),
                new StringDeserializer(),
                new ByteArrayDeserializer());
    }
}
