package ru.kuznetsov.bikeService.models.abstracts;

import jakarta.persistence.MappedSuperclass;
import ru.kuznetsov.bikeService.models.bike.Serviceable;

@MappedSuperclass
public abstract class AbstractServiceableEntity extends AbstractUsableEntity implements Serviceable {
}
