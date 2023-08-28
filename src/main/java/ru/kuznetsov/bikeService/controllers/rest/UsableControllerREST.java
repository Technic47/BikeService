package ru.kuznetsov.bikeService.controllers.rest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import ru.kuznetsov.bikeService.customExceptions.ResourceNotFoundException;
import ru.kuznetsov.bikeService.models.abstracts.AbstractUsableEntity;
import ru.kuznetsov.bikeService.models.dto.AbstractEntityDto;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.services.abstracts.CommonAbstractEntityService;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

public abstract class UsableControllerREST<T extends AbstractUsableEntity,
        S extends CommonAbstractEntityService<T>>
        extends BasicControllerREST<T, S> {

    protected UsableControllerREST(S service) {
        super(service);
    }

//    @Override
//    @GetMapping("/{id}")
//    public AbstractEntityDto show(@PathVariable("id") Long id,
//                                  Principal principal) {
//        Map<Object, Object> response = new HashMap<>();
//        T item = service.getById(id);
//        T show = this.show(item, response, principal);
//        return new AbstractEntityDto(show);
//    }


    @Override
    public AbstractEntityDto create(@RequestBody T item,
                                    @RequestPart(value = "newImage", required = false) MultipartFile file,
                                    Principal principal) {
        this.checkManufacturer(item);
        T updatedItem = this.doCreateProcedure(item, service, file, principal);
        return new AbstractEntityDto(updatedItem);
    }

    @Override
    public AbstractEntityDto update(@PathVariable Long id,
                                    @RequestBody T newItem,
                                    @RequestPart(value = "newImage", required = false) MultipartFile file,
                                    Principal principal) {
        T item = service.getById(id);
        this.checkManufacturer(newItem);
        Map<Object, Object> response = new HashMap<>();
        T updated = this.update(newItem, file, item, response, principal);
        return new AbstractEntityDto(updated);
    }

    void checkManufacturer(T item){
        Long manufacturerId = item.getManufacturer();
        if (manufacturerId != null) {
            if (!manufacturerService.existById(manufacturerId)) {
                throw new ResourceNotFoundException(manufacturerId);
            }
        } else item.setManufacturer(1L);
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
