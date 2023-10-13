package ru.bikeservice.mainresources.models.abstracts.comparators;

import ru.bikeservice.mainresources.models.abstracts.AbstractShowableEntity;

import java.util.Comparator;

public class ComparatorByName implements Comparator<AbstractShowableEntity> {
    @Override
    public int compare(AbstractShowableEntity o1, AbstractShowableEntity o2) {
        return o1.getName().compareToIgnoreCase(o2.getName());
    }
}
