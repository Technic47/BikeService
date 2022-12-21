package ru.kuznetsov.bikeService.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import ru.kuznetsov.bikeService.DAO.DAO;

abstract class GenericController<T> {

    protected final DAO<T> dao;
    @Autowired
    public GenericController(DAO<T> dao) {
        this.dao = dao;
    }

    public DAO<T> getDao() {
        return dao;
    }
}
