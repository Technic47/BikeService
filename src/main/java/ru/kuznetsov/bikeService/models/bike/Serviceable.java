package ru.kuznetsov.bikeService.models.bike;

import ru.kuznetsov.bikeService.models.Showable;
import ru.kuznetsov.bikeService.models.lists.ServiceList;
import ru.kuznetsov.bikeService.models.service.Manufacturer;

public interface Serviceable extends Showable {
    Manufacturer getManufacturer();

    String getPartNumber();

    ServiceList getServiceList();


}
