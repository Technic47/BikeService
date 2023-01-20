package ru.kuznetsov.bikeService.models.servicable;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "bikes")
public class Bike extends Part {
}
