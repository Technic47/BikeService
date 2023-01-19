package ru.kuznetsov.bikeService.models.bike;

import ru.kuznetsov.bikeService.models.Showable;
import ru.kuznetsov.bikeService.models.lists.ServiceList;
import ru.kuznetsov.bikeService.models.service.Usable;

import java.util.List;

public interface Serviceable extends Usable {
    Long getManufacturer();

    String getPartNumber();

    ServiceList returnServiceListObject();

    String getServiceList();

    void addToServiceList(Showable item);

    void delFromServiceList(Showable item);

    String getPartList();

    List<Long> returnPartListObject();

    void addToPartList(Serviceable item);

    public void delFromPartList(Serviceable item);
}
