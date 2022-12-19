package ru.kuznetsov.bikeService.models.service;

public interface Usable {
    int getId();
    Manufacturer getManufacturer();
    String getModel();
    String getName();
    String getInfo();
}
