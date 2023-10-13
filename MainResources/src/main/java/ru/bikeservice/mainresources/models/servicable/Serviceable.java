package ru.bikeservice.mainresources.models.servicable;

import ru.bikeservice.mainresources.models.lists.PartEntity;
import ru.bikeservice.mainresources.models.usable.Usable;

import java.util.Set;

public interface Serviceable extends Usable {
    Set<PartEntity> getLinkedItems();

    void setLinkedItems(Set<PartEntity> linkedItems);
}
