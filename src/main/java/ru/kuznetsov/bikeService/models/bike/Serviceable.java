package ru.kuznetsov.bikeService.models.bike;

import ru.kuznetsov.bikeService.models.Showable;
import ru.kuznetsov.bikeService.models.lists.ServiceList;

public interface Serviceable extends Showable {
    int getManufacturer();

    String getPartNumber();

    ServiceList getServiceList();

    void addToServiceList(Showable item);
}
