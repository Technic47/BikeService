package ru.bikeservice.controllers.notRest;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.bikeservice.mainresources.models.abstracts.AbstractUsableEntity;
import ru.bikeservice.mainresources.services.abstracts.CommonAbstractEntityService;

import java.security.Principal;

@Component
public abstract class UsableController<T extends AbstractUsableEntity,
        S extends CommonAbstractEntityService<T>>
        extends BasicController<T, S> {


    public UsableController(S service) {
        super(service);
    }

    @Override
    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id,
                       Model model,
                       Principal principal) {
        T item = service.getById(id);
        item = this.doShowProcedure(item, principal);
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
        model.addAttribute("manufacturers", manufacturerService.getAll());
        super.addItemAttributesNew(model, item, principal);
    }

    @Override
    protected void addItemAttributesEdit(Model model, T item, Principal principal) {
        model.addAttribute("manufacture", manufacturerService.getById(item.getManufacturer()));
        super.addItemAttributesEdit(model, item, principal);
    }

    @Override
    public ResponseEntity<Resource> createPdf(Long id, Principal principal) {
        T item = service.getById(id);

        return this.prepareUsablePDF(item, principal);
    }
}
