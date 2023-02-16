package ru.kuznetsov.bikeService.models.servicable;

import ru.kuznetsov.bikeService.models.lists.PartEntity;
import ru.kuznetsov.bikeService.models.usable.Usable;

import java.util.List;
import java.util.Set;

public interface Serviceable extends Usable {
    String getPartNumber();

//    ServiceList returnServiceListObject();
//
//    String getServiceList();

    Set<PartEntity> getLinkedItems();

    //    void addToServiceList(Showable item);
//
//    void delFromServiceList(Showable item);
    @Deprecated
    String getPartList();

    @Deprecated
    List<Long> returnPartListObject();

    @Deprecated
    void addToPartList(Serviceable item);

    @Deprecated
    public void delFromPartList(Serviceable item);
}
