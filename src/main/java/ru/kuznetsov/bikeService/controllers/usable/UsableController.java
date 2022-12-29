package ru.kuznetsov.bikeService.controllers.usable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.kuznetsov.bikeService.DAO.DAO;
import ru.kuznetsov.bikeService.controllers.showable.BasicController;
import ru.kuznetsov.bikeService.models.service.Manufacturer;
import ru.kuznetsov.bikeService.models.service.Usable;

@Component
public class UsableController<T extends Usable> extends BasicController<T> {
    protected DAO<Manufacturer> daoManufacturer;

    public UsableController(DAO<T> dao) {
        super(dao);
    }

    @Autowired
    public void setDaoManufacturer(DAO<Manufacturer> daoManufacturer) {
        this.daoManufacturer = daoManufacturer;
        this.daoManufacturer.setCurrentClass(Manufacturer.class);
        this.daoManufacturer.setTableName("manufacturers");
    }

    @Override
    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        int manufactureIndex = dao.show(id).getManufacturer();
        model.addAttribute("manufacture", daoManufacturer.show(manufactureIndex).getName());
        return super.show(id, model);
    }

    @Override
    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        int manufactureIndex = dao.show(id).getManufacturer();
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
