package ru.kuznetsov.bikeService.models.fabric;

import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;
import ru.kuznetsov.bikeService.models.dto.AbstractEntityDto;
import ru.kuznetsov.bikeService.models.dto.AbstractEntityDtoNew;
import ru.kuznetsov.bikeService.models.servicable.Bike;
import ru.kuznetsov.bikeService.models.servicable.Part;
import ru.kuznetsov.bikeService.models.servicable.Serviceable;
import ru.kuznetsov.bikeService.models.showable.Document;
import ru.kuznetsov.bikeService.models.showable.Fastener;
import ru.kuznetsov.bikeService.models.showable.Manufacturer;
import ru.kuznetsov.bikeService.models.showable.Showable;
import ru.kuznetsov.bikeService.models.usable.Consumable;
import ru.kuznetsov.bikeService.models.usable.Tool;
import ru.kuznetsov.bikeService.models.usable.Usable;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.services.abstracts.CommonAbstractEntityService;

import java.util.ArrayList;
import java.util.List;

import static ru.kuznetsov.bikeService.controllers.abstracts.AbstractController.logger;
import static ru.kuznetsov.bikeService.models.users.UserRole.ROLE_ADMIN;
import static ru.kuznetsov.bikeService.models.users.UserRole.ROLE_USER;

public class EntitySupportService {
    /**
     * Converts DTO object to Entity in corresponding category.
     *
     * @param category identification parameter.
     * @param itemDto  DTO to convert.
     * @param <T>      AbstractShowableEntity from main models.
     * @return converted item.
     */
    public static <T extends AbstractShowableEntity> T convertFromDTO(String category, AbstractEntityDtoNew itemDto) {
        T newItem;
        switch (category) {
            case "documents" -> newItem = (T) new Document(itemDto);
            case "fasteners" -> newItem = (T) new Fastener(itemDto);
            case "manufacturers" -> newItem = (T) new Manufacturer(itemDto);
            case "consumables" -> newItem = (T) new Consumable(itemDto);
            case "tools" -> newItem = (T) new Tool(itemDto);
            case "parts" -> newItem = (T) new Part(itemDto);
            case "bikes" -> newItem = (T) new Bike(itemDto);
            default -> throw new IllegalArgumentException("Wrong category argument!");
        }
        return newItem;
    }

    /**
     * Create DTO object from Entity.
     *
     * @param toConvert Entity to convert.
     * @return converted DTO.
     */
    public static AbstractEntityDto createDtoFrom(Showable toConvert) {
        AbstractEntityDto newDto = new AbstractEntityDto();
        if (toConvert instanceof Usable) {
            newDto.setManufacturer(((Usable) toConvert).getManufacturer());
            newDto.setModel(((Usable) toConvert).getModel());
        }
        if (toConvert instanceof Serviceable) {
            newDto.setLinkedItems(((Serviceable) toConvert).getLinkedItems());
        }
        newDto.setId(toConvert.getId());
        newDto.setName(toConvert.getName());
        newDto.setDescription(toConvert.getDescription());
        newDto.setPicture(toConvert.getPicture());
        newDto.setLink(toConvert.getLink());
        newDto.setValue(toConvert.getValue());

        return newDto;
    }

    /**
     * Converts List of entities to representable DTO.
     *
     * @param objects List to covert.
     * @param <T>     AbstractShowableEntity from main models.
     * @return converted List.
     */
    public static <T extends AbstractShowableEntity> List<AbstractEntityDto> convertListToDto(
            List<T> objects) {
        List<AbstractEntityDto> indexList = new ArrayList<>();
        if (objects != null) {
            objects.forEach(object -> indexList.add(createDtoFrom(object)));
        }
        return indexList;
    }

    /**
     * Creates List of entities depending on userModel role and shared flag.
     *
     * @param service   service for entities.
     * @param userModel user for whom List is being created.
     * @param category  category of entities for logging.
     * @param shared    flag for including shared entities.
     * @param <T>       AbstractShowableEntity from main models.
     * @param <S>
     * @return formed List.
     */
    public static <T extends AbstractShowableEntity,
            S extends CommonAbstractEntityService<T>> List<T> buildIndexList(
            final S service, UserModel userModel, String category, boolean shared) {
        List<T> objects = null;

        if (userModel.getAuthorities().contains(ROLE_USER)) {
            if (shared) {
                objects = service.findByCreatorOrShared(userModel.getId());
                logger.info("Personal and shared " + category + " are shown to '" + userModel.getUsername() + "'");
            } else {
                objects = service.findByCreator(userModel.getId());
                logger.info("Personal " + category + " are shown to '" + userModel.getUsername() + "'");
            }
        }
        if (userModel.getAuthorities().contains(ROLE_ADMIN)) {
            objects = service.index();
            logger.info("All " + category + " are shown to " + userModel.getUsername() + "'");
        }
        return objects;
    }
}
