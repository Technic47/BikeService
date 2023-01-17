//package ru.kuznetsov.bikeService.controllers;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;
//import ru.kuznetsov.bikeService.repositories.CommonRepository;
//
//@Component
//public abstract class AbstractController<T extends AbstractShowableEntity, R extends CommonRepository<T>> {
//    protected final R repository;
//
//    @Autowired
//    public AbstractController(R repository) {
//        this.repository = repository;
//    }
//}
