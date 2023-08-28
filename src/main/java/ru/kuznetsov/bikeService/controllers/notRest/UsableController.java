package ru.kuznetsov.bikeService.controllers.notRest;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.kuznetsov.bikeService.models.abstracts.AbstractUsableEntity;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.services.abstracts.CommonAbstractEntityService;

import java.security.Principal;

@Component
public abstract class UsableController<T extends AbstractUsableEntity, S extends CommonAbstractEntityService<T>>
        extends BasicController<T, S> {
    public UsableController(S service) {
        super(service);
    }


    @Operation(hidden = true)
    @Override
    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id,
                       Model model,
                       Principal principal) {
        T item = service.getById(id);
        if (item == null) {
            return "redirect:/" + category;
        }
        Long manufactureIndex = item.getManufacturer();
        model.addAttribute("manufacture", manufacturerService.getById(manufactureIndex).getName());
        this.addItemAttributesShow(model, item, principal);
        return super.show(item, model, principal, category);
    }


    @Override
    protected void addItemAttributesNew(Model model, T item, Principal principal) {
        model.addAttribute("manufacturers", manufacturerService.index());
        super.addItemAttributesNew(model, item, principal);
    }

    @Override
    protected void addItemAttributesEdit(Model model, T item, Principal principal) {
        model.addAttribute("manufacture", manufacturerService.getById(item.getManufacturer()));
        super.addItemAttributesEdit(model, item, principal);
    }


    @Override
    protected void preparePDF(T item, Principal principal) {
        this.pdfService.addManufacturer(this.manufacturerService.getById(item.getManufacturer()));
        UserModel userModel = this.getUserModelFromPrincipal(principal);
        this.pdfService.newPDFDocument()
                .addUserName(userModel.getUsername())
                .addImage(this.pictureService.getById(item.getPicture()).getName())
                .buildUsable(item);
    }
}
