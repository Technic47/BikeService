package ru.kuznetsov.spokecalc;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CalcService {
    @KafkaListener(topics = "spokeCalc", id = "spokeCalculator")
    @SendTo
    public double calculateSide(Map<String, Double> data) {
        double N = data.get("number_of_spokes");
        double K = data.get("number_of_crosses");
        double OLD = data.get("OLD");
        double ERD = data.get("ERD");
        double rOff = data.get("rim_offset");
        double fOff = data.get("frame_offset");
        double holeDistance = data.get("distance_between_holes");
        double holeDiam = data.get("flange_hole_diameter");
        double hOffL = data.get("spoke_hole_offset");
        double outerSide = data.get("distance_out_flange");

        double res = (((360 / (N / 2)) * K) * Math.PI) / 180;
        double cos = Math.cos(res);

        double innerWidth = this.getInnerDistance(data.get("side"), OLD, outerSide, rOff, fOff);

        double leftCalc = preCalc(ERD, holeDistance, cos);
        return calcMain(innerWidth, leftCalc, holeDiam, hOffL);
    }

    private double getInnerDistance(double side, double OLD, double outerSide, double rimOffset, double frameOffset) {
        double width;
        switch ((int) side) {
            case 1 -> {
                width = (OLD / 2 - outerSide)
                        - rimOffset - frameOffset;
                return width;
            }
            case 2 -> {
                width = (OLD / 2 - outerSide)
                        + rimOffset + frameOffset;
                return width;
            }
            default -> {
                return 0;
            }
        }
    }

    private double preCalc(double ERD, double distanceBetweenHoles, double cos) {
        return Math.sqrt(
                Math.pow(ERD / 2, 2)
                        + Math.pow(distanceBetweenHoles / 2, 2)
                        - 2 * (ERD / 2) * (distanceBetweenHoles / 2) * cos);
    }

    private double calcMain(double w, double preCalc, double holeDiam, double holeOffset) {
        return Math.round(
                Math.sqrt(
                        Math.pow(w, 2)
                                + Math.pow(preCalc, 2)
                )
                        - holeDiam / 2 + holeOffset);
    }
}
