package ru.kuznetsov.bikeService.models.bike;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "bikes")
public class Bike extends Part {
}
