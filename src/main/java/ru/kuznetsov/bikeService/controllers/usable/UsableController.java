package ru.kuznetsov.bikeService.controllers.usable;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.kuznetsov.bikeService.DAO.DAO;
import ru.kuznetsov.bikeService.controllers.showable.BasicController;
import ru.kuznetsov.bikeService.models.service.Manufacturer;
import ru.kuznetsov.bikeService.models.service.Usable;

public class UsableController<T extends Usable> extends BasicController<T> {
    protected final DAO<Manufacturer> dao2;

    public UsableController(DAO<T> dao, DAO<Manufacturer> dao2, String currentObjectName, T newObject) {
        super(dao, currentObjectName, newObject);
        this.dao2 = dao2;
        this.dao2.setCurrentClass(Manufacturer.class);
        this.dao2.setTableName("manufacturers");
    }

    @Override
    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        int manufactureIndex = dao.show(id).getManufacturer();
        model.addAttribute("manufacture", dao2.show(manufactureIndex).getName());
        return super.show(id, model);
    }

    @Override
    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        int manufactureIndex = dao.show(id).getManufacturer();
        model.addAttribute("manufacture", dao2.show(manufactureIndex));
        model.addAttribute("manufacturers", dao2.index());
        return super.edit(model, id);
    }

    @Override
    @GetMapping("/new")
    public String newItem(Model model) {
        model.addAttribute("manufacturers", dao2.index());
        return super.newItem(model);
    }
}
