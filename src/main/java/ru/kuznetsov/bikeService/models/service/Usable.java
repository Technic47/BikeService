package ru.kuznetsov.bikeService.models.service;

import ru.kuznetsov.bikeService.models.Showable;

public interface Usable extends Showable {
    Long getManufacturer();
    String getModel();
}
