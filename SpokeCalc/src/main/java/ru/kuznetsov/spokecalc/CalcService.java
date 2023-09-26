package ru.kuznetsov.spokecalc;

import org.springframework.stereotype.Service;
import ru.kuznetsov.spokecalc.model.CalcData;

import java.util.Map;

@Service
public class CalcService {
//    private CalcRepository repository;

    public double calculateSide(Map<String, Double> data, Sides side) {
        double N = data.get("Number_of_spokes");
        double K = data.get("Number_of_crosses");
        double OLD = data.get("OLD");
        double ERD = data.get("ERD");
        double rOff = data.get("Rim_offset");
        double fOff = data.get("Frame_offset");
        double holeDistance = data.get("Distance_between_holes");
        double holeDiam = data.get("Flange_hole_diameter");
        double hOffL = data.get("Spoke_hole_offset");
        double outerSide = data.get("Distance_out_flange");

        double res = (((360 / (N / 2)) * K) * Math.PI) / 180;
        double cos = Math.cos(res);

        double innerWidth = this.getInnerDistance(side, OLD, outerSide, rOff, fOff);

        double leftCalc = preCalc(ERD, holeDistance, cos);
        return calcMain(innerWidth, leftCalc, holeDiam, hOffL);
    }

    private double getInnerDistance(Sides side, double OLD, double outerSide, double rimOffset, double frameOffset) {
        double width;
        switch (side) {
            case LEFT -> {
                    width = (OLD / 2 - outerSide)
                            - rimOffset - frameOffset;
                return width;
            }
            case RIGHT -> {
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


    private CalcData updateFields(CalcData oldData, CalcData newData) {
        oldData.setFlange_hole_diameter(newData.getFlange_hole_diameter());
        oldData.setDistance_between_holes(newData.getDistance_between_holes());
        oldData.setSpoke_hole_offset(newData.getSpoke_hole_offset());
        oldData.setDistance_out_flange(newData.getDistance_out_flange());
        oldData.setNumber_of_spokes(newData.getNumber_of_spokes());
        oldData.setNumber_of_crosses(newData.getNumber_of_crosses());
        oldData.setOld(newData.getOld());
        oldData.setErd(newData.getErd());
        oldData.setRim_offset(newData.getRim_offset());
        oldData.setFrame_offset(newData.getFrame_offset());
        return oldData;
    }
}
