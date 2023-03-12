package ru.kuznetsov.bikeService.models.servicable;

import ru.kuznetsov.bikeService.models.lists.PartEntity;
import ru.kuznetsov.bikeService.models.usable.Usable;

import java.util.Set;

public interface Serviceable extends Usable {
    Set<PartEntity> getLinkedItems();
    void setLinkedItems(Set<PartEntity> linkedItems);
}
