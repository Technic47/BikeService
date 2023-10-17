package ru.kuznetsov.bikeService.controllers.rest;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import ru.bikeservice.mainresources.customExceptions.ResourceNotFoundException;
import ru.bikeservice.mainresources.models.abstracts.AbstractUsableEntity;
import ru.bikeservice.mainresources.models.dto.AbstractEntityDto;
import ru.bikeservice.mainresources.models.dto.AbstractEntityDtoNew;
import ru.bikeservice.mainresources.models.support.EntitySupportService;
import ru.bikeservice.mainresources.services.abstracts.CommonAbstractEntityService;

import java.security.Principal;

import static ru.bikeservice.mainresources.models.support.EntitySupportService.convertFromDTO;
import static ru.bikeservice.mainresources.models.support.EntitySupportService.createDtoFrom;

public abstract class UsableControllerREST<T extends AbstractUsableEntity,
        S extends CommonAbstractEntityService<T>>
        extends BasicControllerREST<T, S> {
    protected UsableControllerREST(S service) {
        super(service);
    }

    @Override
    public AbstractEntityDto create(@RequestBody AbstractEntityDtoNew itemDto,
                                    @RequestPart(value = "newImage", required = false) MultipartFile file,
                                    Principal principal) {
        T item = EntitySupportService.convertFromDTO(category, itemDto);
        this.checkManufacturer(item);
        T updatedItem = this.doCreateProcedure(item, service, file, principal);
        return createDtoFrom(updatedItem);
    }

    @Override
    public AbstractEntityDto update(@PathVariable Long id,
                                    @RequestBody AbstractEntityDtoNew newItem,
                                    @RequestPart(value = "newImage", required = false) MultipartFile file,
                                    Principal principal) {
        T item = service.getById(id);
        T newEntity = convertFromDTO(category, newItem);
        this.checkManufacturer(newEntity);
        T updated = this.update(newEntity, file, item, principal);
        return createDtoFrom(updated);
    }

    void checkManufacturer(T item) {
        Long manufacturerId = item.getManufacturer();
        if (manufacturerId != null) {
            if (!manufacturerService.existById(manufacturerId)) {
                throw new ResourceNotFoundException(manufacturerId);
            }
        } else item.setManufacturer(1L);
    }

    @Override
    public ResponseEntity<Resource> createPdf(@PathVariable Long id, Principal principal) {
        T item = this.service.getById(id);
        return this.prepareUsablePDF(item, principal);
    }
}
