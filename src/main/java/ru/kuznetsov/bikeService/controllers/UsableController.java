package ru.kuznetsov.bikeService.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.kuznetsov.bikeService.models.abstracts.AbstractUsableEntity;
import ru.kuznetsov.bikeService.services.ManufacturerService;
import ru.kuznetsov.bikeService.services.abstracts.CommonAbstractEntityService;

import java.security.Principal;

@Component
@Scope("prototype")
public class UsableController<T extends AbstractUsableEntity, S extends CommonAbstractEntityService<T>>
        extends BasicController<T, S> {
    protected ManufacturerService daoManufacturer;

    public UsableController(S dao) {
        super(dao);
    }


    @Override
    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id,
                       Principal principal,
                       Model model) {
        Long manufactureIndex = dao.show(id).getManufacturer();
        model.addAttribute("manufacture", daoManufacturer.show(manufactureIndex).getName());
        return super.show(id, principal, model);
    }

//    @Override
//    @GetMapping("/{id}/edit")
//    public String edit(Model model, @PathVariable("id") Long id) {
////        Long manufactureIndex = dao.show(id).getManufacturer();
////        model.addAttribute("manufacture", daoManufacturer.show(manufactureIndex));
////        model.addAttribute("manufacturers", daoManufacturer.index());
//        this.addItemAttributes(model, dao.show(id));
//        return super.edit(model, id);
//    }


    @Override
    protected void addItemAttributes(Model model, T item) {
        model.addAttribute("manufacture", daoManufacturer.show(item.getManufacturer()));
        model.addAttribute("manufacturers", daoManufacturer.index());
        super.addItemAttributes(model, item);
    }

    @Autowired
    public void setDaoManufacturer(ManufacturerService daoManufacturer) {
        this.daoManufacturer = daoManufacturer;
    }
}
