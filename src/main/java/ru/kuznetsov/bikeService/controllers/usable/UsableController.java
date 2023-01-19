package ru.kuznetsov.bikeService.controllers.usable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.kuznetsov.bikeService.DAO.DAO;
import ru.kuznetsov.bikeService.controllers.showable.BasicController;
import ru.kuznetsov.bikeService.models.abstracts.BaseEntity;
import ru.kuznetsov.bikeService.models.service.Manufacturer;
import ru.kuznetsov.bikeService.models.service.Usable;
import ru.kuznetsov.bikeService.repositories.services.CommonService;

@Component
public class UsableController<T extends BaseEntity & Usable> extends BasicController<T> {
    protected DAO<Manufacturer> daoManufacturer;

    public UsableController(CommonService<T> dao) {
        super(dao);
    }

//    public UsableController(DAO<T> dao) {
//        super(dao);
//    }

    @Autowired
    public void setDaoManufacturer(DAO<Manufacturer> daoManufacturer) {
        this.daoManufacturer = daoManufacturer;
        this.daoManufacturer.setCurrentClass(Manufacturer.class);
    }

    @Override
    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id, Model model) {
        Long manufactureIndex = dao.show(id).getManufacturer();
        model.addAttribute("manufacture", daoManufacturer.show(manufactureIndex).getName());
        return super.show(id, model);
    }

    @Override
    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id) {
        Long manufactureIndex = dao.show(id).getManufacturer();
        model.addAttribute("manufacture", daoManufacturer.show(manufactureIndex));
        model.addAttribute("manufacturers", daoManufacturer.index());
        return super.edit(model, id);
    }

    @Override
    @GetMapping("/new")
    public String newItem(Model model) {
        model.addAttribute("manufacturers", daoManufacturer.index());
        return super.newItem(model);
    }
}
