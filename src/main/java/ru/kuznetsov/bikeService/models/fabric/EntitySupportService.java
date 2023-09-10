package ru.kuznetsov.bikeService.models.fabric;

import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;
import ru.kuznetsov.bikeService.models.abstracts.comparators.ComparatorByName;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    public static <T extends AbstractShowableEntity> List<T> sortBasic(List<T> list, String sort) {
        List<T> sortedList;
        switch (sort) {
            case "ASC", "asc" -> sortedList = list.stream().sorted(new ComparatorByName()).toList();
            case "DESC", "desc" ->
                    sortedList = list.stream().sorted(new ComparatorByName()).sorted(Collections.reverseOrder()).toList();
            default -> sortedList = list;
        }
        return sortedList;
    }
}
