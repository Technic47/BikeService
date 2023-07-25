package ru.kuznetsov.bikeService.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.kuznetsov.bikeService.models.abstracts.AbstractUsableEntity;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.services.abstracts.CommonAbstractEntityService;
import ru.kuznetsov.bikeService.services.modelServices.ManufacturerService;

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
                       Model model,
                       Principal principal) {
        T item = service.show(id);
        if (item == null) {
            return "redirect:/" + category;
        }
        Long manufactureIndex = item.getManufacturer();
        model.addAttribute("manufacture", manufacturerService.show(manufactureIndex).getName());
        return super.show(item, model, principal);
    }


    @Override
    protected void addItemAttributesNew(Model model, T item, Principal principal) {
        model.addAttribute("manufacturers", manufacturerService.index());
        super.addItemAttributesNew(model, item, principal);
    }

    @Override
    protected void addItemAttributesEdit(Model model, T item, Principal principal) {
        model.addAttribute("manufacture", manufacturerService.show(item.getManufacturer()));
        super.addItemAttributesEdit(model, item, principal);
    }


    @Override
    protected void preparePDF(T item, Principal principal) {
        this.pdfService.addManufacturer(this.manufacturerService.show(item.getManufacturer()));
        UserModel userModel = this.getUserModelFromPrincipal(principal);
        this.pdfService.newPDFDocument()
                .addUserName(userModel.getUsername())
                .addImage(this.pictureService.show(item.getPicture()).getName())
                .buildUsable(item);
    }

    @Autowired
    public void setManufacturerService(ManufacturerService manufacturerService) {
        this.manufacturerService = manufacturerService;
    }
}
