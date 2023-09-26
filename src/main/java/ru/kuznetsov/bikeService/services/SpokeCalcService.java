package ru.kuznetsov.bikeService.services;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.requestreply.ReplyingKafkaTemplate;
import org.springframework.kafka.requestreply.RequestReplyFuture;
import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.dto.CalcDataDto;

import java.util.Map;

@Service
public class SpokeCalcService {
    private final ReplyingKafkaTemplate<String, Map<String, Double>, Double> spokeCalcKafkaTemplate;

    public SpokeCalcService(ReplyingKafkaTemplate<String, Map<String, Double>, Double> spokeCalcKafkaTemplate) {
        this.spokeCalcKafkaTemplate = spokeCalcKafkaTemplate;
    }

    public double calculate(CalcDataDto data) {
        ProducerRecord<String, Map<String, Double>> record = new ProducerRecord<>("spokeCalc", data.makeMap());
        RequestReplyFuture<String, Map<String, Double>, Double> reply = spokeCalcKafkaTemplate.sendAndReceive(record);
        try {
            return reply.get().value();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
