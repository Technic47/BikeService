package ru.kuznetsov.bikeService.models.usable;

import ru.kuznetsov.bikeService.models.showable.Showable;

public interface Usable extends Showable {
    Long getManufacturer();
    String getModel();
}
