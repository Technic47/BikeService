package ru.bikeservice.mainresources.models.dto;

import java.util.HashMap;
import java.util.Map;

public class CalcDataDto {
    // 1.0 - left, 2.0 - right(drive)
    private double side;
    private double flange_hole_diameter;
    private double distance_between_holes;
    private double spoke_hole_offset;
    private double distance_out_flange;
    private double number_of_spokes;
    private double number_of_crosses;
    private double old;
    private double erd;
    private double rim_offset;
    private double frame_offset;

    public CalcDataDto() {
    }

    public Map<String, Double> makeMap() {
        Map<String, Double> map = new HashMap<>();
        map.put("side", side);
        map.put("flange_hole_diameter", flange_hole_diameter);
        map.put("distance_between_holes", distance_between_holes);
        map.put("spoke_hole_offset", spoke_hole_offset);
        map.put("distance_out_flange", distance_out_flange);
        map.put("number_of_spokes", number_of_spokes);
        map.put("number_of_crosses", number_of_crosses);
        map.put("OLD", old);
        map.put("ERD", erd);
        map.put("rim_offset", rim_offset);
        map.put("frame_offset", frame_offset);

        return map;
    }

    public double getSide() {
        return side;
    }

    public void setSide(double side) {
        this.side = side;
    }

    public double getFlange_hole_diameter() {
        return flange_hole_diameter;
    }

    public void setFlange_hole_diameter(double hole_diameter_left) {
        this.flange_hole_diameter = hole_diameter_left;
    }

    public double getDistance_between_holes() {
        return distance_between_holes;
    }

    public void setDistance_between_holes(double distance_between_holes) {
        this.distance_between_holes = distance_between_holes;
    }

    public double getSpoke_hole_offset() {
        return spoke_hole_offset;
    }

    public void setSpoke_hole_offset(double spoke_hole_offset) {
        this.spoke_hole_offset = spoke_hole_offset;
    }

    public double getDistance_out_flange() {
        return distance_out_flange;
    }

    public void setDistance_out_flange(double distance_out_flange) {
        this.distance_out_flange = distance_out_flange;
    }

    public double getNumber_of_spokes() {
        return number_of_spokes;
    }

    public void setNumber_of_spokes(double number_of_spokes) {
        this.number_of_spokes = number_of_spokes;
    }

    public double getNumber_of_crosses() {
        return number_of_crosses;
    }

    public void setNumber_of_crosses(double number_of_crosses) {
        this.number_of_crosses = number_of_crosses;
    }

    public double getOld() {
        return old;
    }

    public void setOld(double OLD) {
        this.old = OLD;
    }

    public double getErd() {
        return erd;
    }

    public void setErd(double ERD) {
        this.erd = ERD;
    }

    public double getRim_offset() {
        return rim_offset;
    }

    public void setRim_offset(double rim_offset) {
        this.rim_offset = rim_offset;
    }

    public double getFrame_offset() {
        return frame_offset;
    }

    public void setFrame_offset(double frame_offset) {
        this.frame_offset = frame_offset;
    }
}
