package ru.kuznetsov.bikeService.models.showable;

import java.util.Date;

public interface Showable {
    Long getId();
    String getName();
    String getValue();
    void setValue(String value);
    String getValueName();
    String getDescription();
    String getLink();
    Long getPicture();
    void setPicture(Long id);
    Long getCreator();
    void setCreator(Long id);
    boolean getIsShared();
    void setIsShared(boolean shared);
    String getCredentials();
    Date getCreated();

    void setCreated(Date created);

    Date getUpdated();

    void setUpdated(Date updated);
}
