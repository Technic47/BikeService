package ru.bikeservice.mainresources.models.usable;

import ru.bikeservice.mainresources.models.showable.Showable;

public interface Usable extends Showable {
    Long getManufacturer();
    String getModel();
}
