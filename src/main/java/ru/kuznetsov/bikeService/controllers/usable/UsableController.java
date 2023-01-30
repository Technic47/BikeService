package ru.kuznetsov.bikeService.controllers.usable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.kuznetsov.bikeService.controllers.showable.BasicController;
import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;
import ru.kuznetsov.bikeService.models.usable.Usable;
import ru.kuznetsov.bikeService.services.ManufacturerService;
import ru.kuznetsov.bikeService.services.abstracts.CommonAbstractShowableEntityService;

@Component
@Scope("prototype")
public class UsableController<T extends AbstractShowableEntity & Usable, S extends CommonAbstractShowableEntityService<T>>
        extends BasicController<T, S> {
    protected ManufacturerService daoManufacturer;

    public UsableController(S dao) {
        super(dao);
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

    @Autowired
    public void setDaoManufacturer(ManufacturerService daoManufacturer) {
        this.daoManufacturer = daoManufacturer;
    }
}
