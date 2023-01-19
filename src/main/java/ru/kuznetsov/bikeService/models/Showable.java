package ru.kuznetsov.bikeService.models;

public interface Showable {
    Long getId();
    String getName();
    String getValue();
    void setValue(String value);
    String getDescription();
    Long getPicture();
    void setPicture(Long id);
}
