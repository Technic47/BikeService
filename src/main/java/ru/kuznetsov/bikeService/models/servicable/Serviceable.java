package ru.kuznetsov.bikeService.models.servicable;

import ru.kuznetsov.bikeService.models.lists.PartEntity;
import ru.kuznetsov.bikeService.models.lists.ServiceList;
import ru.kuznetsov.bikeService.models.showable.Showable;
import ru.kuznetsov.bikeService.models.usable.Usable;

import java.util.List;

public interface Serviceable extends Usable {
    String getPartNumber();

    ServiceList returnServiceListObject();

    String getServiceList();

    List<PartEntity> getLinkedItems();

    void addToServiceList(Showable item);

    void delFromServiceList(Showable item);

    String getPartList();

    List<Long> returnPartListObject();

    void addToPartList(Serviceable item);

    public void delFromPartList(Serviceable item);
}
