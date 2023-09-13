package ru.kuznetsov.bikeService.controllers.rest;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import ru.kuznetsov.bikeService.customExceptions.ResourceNotFoundException;
import ru.kuznetsov.bikeService.models.abstracts.AbstractUsableEntity;
import ru.kuznetsov.bikeService.models.dto.AbstractEntityDto;
import ru.kuznetsov.bikeService.models.dto.AbstractEntityDtoNew;
import ru.kuznetsov.bikeService.models.dto.PdfEntityDto;
import ru.kuznetsov.bikeService.models.fabric.EntitySupportService;
import ru.kuznetsov.bikeService.models.pictures.Picture;
import ru.kuznetsov.bikeService.models.showable.Manufacturer;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.services.abstracts.CommonAbstractEntityService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;

import static ru.kuznetsov.bikeService.config.SpringConfig.UPLOAD_PATH;
import static ru.kuznetsov.bikeService.models.fabric.EntitySupportService.convertFromDTO;
import static ru.kuznetsov.bikeService.models.fabric.EntitySupportService.createDtoFrom;

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
        UserModel userModel = getUserModelFromPrincipal(principal);
        Manufacturer manufacturer = manufacturerService.getById(item.getManufacturer());
        Picture picture = pictureService.getById(item.getPicture());
        Path path = Paths.get(UPLOAD_PATH + "/preview/" + picture.getName());

        try {
            PdfEntityDto body = new PdfEntityDto(item, userModel.getUsername(), Files.readAllBytes(path), manufacturer.getName());
            return preparePDF(body);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
