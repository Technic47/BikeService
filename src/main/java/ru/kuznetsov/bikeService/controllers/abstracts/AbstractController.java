//package ru.kuznetsov.bikeService.controllers.abstracts;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import ru.kuznetsov.bikeService.models.abstracts.AbstractEntity;
//import ru.kuznetsov.bikeService.services.abstracts.CommonService;
//
//@Component
//public abstract class AbstractController<T extends AbstractEntity, S extends CommonService<T>>
//        implements CommonController<T>
//{
//    private final S service;
//
//    @Autowired
//    protected AbstractController(S service) {
//        this.service = service;
//    }
//
//
//}
