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
    protected ManufacturerService manufacturerService;

    public UsableController(S service) {
        super(service);
    }


    @Override
    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id,
                       Principal principal,
                       Model model) {
        Long manufactureIndex = service.show(id).getManufacturer();
        model.addAttribute("manufacture", manufacturerService.show(manufactureIndex).getName());
        return super.show(id, principal, model);
    }


    @Override
    protected void addItemAttributesNew(Model model, T item) {
        model.addAttribute("manufacturers", manufacturerService.index());
        super.addItemAttributesNew(model, item);
    }

    @Override
    protected void addItemAttributesEdit(Model model, T item) {
        model.addAttribute("manufacture", manufacturerService.show(item.getManufacturer()));
        super.addItemAttributesEdit(model, item);
    }


    @Override
    protected void preparePDF(T item) {
        this.pdfService.addManufactorer(this.manufacturerService.show(item.getManufacturer()));
        super.preparePDF(item);
    }

    @Autowired
    public void setManufacturerService(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }
}
