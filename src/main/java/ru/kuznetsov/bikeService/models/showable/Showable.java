package ru.kuznetsov.bikeService.models.showable;

public interface Showable {
    Long getId();
    String getName();
    String getValue();
    void setValue(String value);
    String getDescription();
    String getLink();
    Long getPicture();
    void setPicture(Long id);
    Long getCreator();
    void setCreator(Long id);
}
