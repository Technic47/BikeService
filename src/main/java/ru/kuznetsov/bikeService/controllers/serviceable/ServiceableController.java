package ru.kuznetsov.bikeService.controllers.serviceable;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.kuznetsov.bikeService.DAO.DAO;
import ru.kuznetsov.bikeService.controllers.usable.UsableController;
import ru.kuznetsov.bikeService.models.bike.Serviceable;
import ru.kuznetsov.bikeService.models.lists.ServiceList;
import ru.kuznetsov.bikeService.models.service.Manufacturer;
import ru.kuznetsov.bikeService.models.service.Usable;

public class ServiceableController<T extends Serviceable & Usable> extends UsableController<T> {
    protected ServiceList cacheList;

    public ServiceableController(DAO<T> dao, DAO<Manufacturer> dao2, String currentObjectName, T newObject) {
        super(dao, dao2, currentObjectName, newObject);
        this.dao2.setCurrentClass(Manufacturer.class);
        this.dao2.setTableName("manufacturers");
        this.cacheList = new ServiceList();
    }

    @Override
    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {

        this.cacheList = dao.show(id).getServiceList();
        model.addAttribute("serviceList", cacheList);
        return super.show(id, model);
    }

}
