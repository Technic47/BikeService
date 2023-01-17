package ru.kuznetsov.bikeService.models.abstracts;

import jakarta.persistence.MappedSuperclass;
import ru.kuznetsov.bikeService.models.service.Usable;

@MappedSuperclass
public abstract class AbstractUsableEntity extends AbstractShowableEntity implements Usable {
}
