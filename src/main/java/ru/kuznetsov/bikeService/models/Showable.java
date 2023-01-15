package ru.kuznetsov.bikeService.models;

public interface Showable {
    int getId();
    String getName();
    String getValue();
    String getDescription();
    int getPicture();
    void setPicture(int id);
}
