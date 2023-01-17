package ru.kuznetsov.bikeService.models;

public interface Showable {
    Long getId();
    String getName();
    String getValue();
    void setValue(String value);
    String getDescription();
    int getPicture();
    void setPicture(int id);
}
