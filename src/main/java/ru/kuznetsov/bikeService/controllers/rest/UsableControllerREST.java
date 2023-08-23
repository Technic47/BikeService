package ru.kuznetsov.bikeService.controllers.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.kuznetsov.bikeService.models.abstracts.AbstractUsableEntity;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.services.PDFService;
import ru.kuznetsov.bikeService.services.abstracts.CommonAbstractEntityService;
import ru.kuznetsov.bikeService.services.modelServices.ManufacturerService;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

public abstract class UsableControllerREST<T extends AbstractUsableEntity,
        S extends CommonAbstractEntityService<T>>
        extends BasicControllerREST<T, S> {
    protected final ManufacturerService manufacturerService;

    protected UsableControllerREST(S service, PDFService pdfService, ManufacturerService manufacturerService) {
        super(service, pdfService);
        this.manufacturerService = manufacturerService;
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity show(@PathVariable("id") Long id,
                               Principal principal) {
        Map<Object, Object> response = new HashMap<>();
        T item = service.getById(id);
        if (item == null) {
            throw new RuntimeException("Item not found!");
        }
//        Long manufactureIndex = item.getManufacturer();
//        response.put("manufacture", manufacturerService.getById(manufactureIndex).getName());
        this.show(item, response, principal);
        return ResponseEntity.ok(response);
    }

    @Override
    protected void addItemAttributesShow(Map<Object, Object> response, T item, Principal principal) {
        Long manufactureIndex = item.getManufacturer();
        response.put("manufacture", manufacturerService.getById(manufactureIndex).getName());
        super.addItemAttributesShow(response, item, principal);
    }

    @Override
    protected void addItemAttributesNew(Map<Object, Object> response, T item, Principal principal) {
//        response.put("manufacturers", manufacturerService.index());
        super.addItemAttributesNew(response, item, principal);
    }

    @Override
    protected void addItemAttributesEdit(Map<Object, Object> response, T item, Principal principal) {
        response.put("manufacture", manufacturerService.getById(item.getManufacturer()));
        super.addItemAttributesEdit(response, item, principal);
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
