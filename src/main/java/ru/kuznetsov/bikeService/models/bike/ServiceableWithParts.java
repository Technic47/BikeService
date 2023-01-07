package ru.kuznetsov.bikeService.models.bike;

import java.util.List;
import java.util.Map;

public interface ServiceableWithParts extends Serviceable {

    String getPartList();

    Map<String, List<Integer>> returnPartListObject();

    void addToPartList(Serviceable item);

    public void delFromPartList(Serviceable item);
}
