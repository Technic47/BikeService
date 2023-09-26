package ru.kuznetsov.spokecalc;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static ru.kuznetsov.spokecalc.Sides.LEFT;

@RestController
@RequestMapping("/spokeCalk")
public class CalkController {
    private final CalcService service;

    public CalkController(CalcService service) {
        this.service = service;
    }

//    @PostMapping("/calcLeft")
//    public ResponseEntity calcLeft(@RequestBody CalcData data){
//        double spokeSize = this.service.calculateSide(data, LEFT);
//        Map<String, Double> response = new HashMap<>();
//        response.put("LeftSpokeSize", spokeSize);
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("/calcRight")
//    public ResponseEntity calcRight(@RequestBody CalcData data){
//        double spokeSize = this.service.calculateSide(data, RIGHT);
//        Map<String, Double> response = new HashMap<>();
//        response.put("LeftSpokeSize", spokeSize);
//        return ResponseEntity.ok(response);
//    }

    @KafkaListener(topics = "spokeCalc", id = "spokeCalculator")
    @SendTo
    public double calcLeft(Map<String, Double> data){
        return service.calculateSide(data, LEFT);
    }
}
