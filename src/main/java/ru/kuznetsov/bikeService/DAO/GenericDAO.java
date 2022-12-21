package ru.kuznetsov.bikeService.DAO;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class GenericDAO<T> {
    private Class<T> inferedClass;

}
