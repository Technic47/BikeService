package ru.kuznetsov.bikeService.controllers.rest;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kuznetsov.bikeService.customExceptions.AccessToResourceDenied;
import ru.kuznetsov.bikeService.models.abstracts.AbstractServiceableEntity;
import ru.kuznetsov.bikeService.models.dto.AbstractEntityDto;
import ru.kuznetsov.bikeService.models.lists.PartEntity;
import ru.kuznetsov.bikeService.models.showable.Showable;
import ru.kuznetsov.bikeService.services.abstracts.CommonServiceableEntityService;

import java.security.Principal;

public abstract class ServiceableControllerREST<T extends AbstractServiceableEntity,
        S extends CommonServiceableEntityService<T>>
        extends UsableControllerREST<T, S> {
    protected ServiceableControllerREST(S service) {
        super(service);
    }

    @Operation(summary = "Add linked item to entity")
    @PutMapping("/{itemId}/linkedItems/{linkedItemId}")
    public AbstractEntityDto addItemToItemList(@PathVariable Long itemId,
                                               @PathVariable Long linkedItemId,
                                               @RequestParam(value = "type") String type,
                                               @RequestParam(value = "amount") Integer amount,
                                               Principal principal) {
        T item = service.getById(itemId);
        Showable itemToAdd = this.checkItem(linkedItemId, type);
        if (this.checkAccessToItem(item, principal)) {
            T updated = this.itemsManipulation(item, 1, itemToAdd.getClass(), linkedItemId, amount);
            return new AbstractEntityDto(updated);
        } else throw new AccessToResourceDenied(itemToAdd.getId());
    }

    @Operation(summary = "Del linked item from entity")
    @DeleteMapping("/{itemId}/linkedItems/{linkedItemId}")
    public AbstractEntityDto delItemFromItemList(@PathVariable Long itemId,
                                                 @PathVariable Long linkedItemId,
                                                 @RequestParam(value = "type") String type,
                                                 @RequestParam(value = "amount") Integer amount,
                                                 Principal principal) {
        T item = service.getById(itemId);
        Showable itemToAdd = this.checkItem(linkedItemId, type);
        if (this.checkAccessToItem(item, principal)) {
            T updated = this.itemsManipulation(item, 0, itemToAdd.getClass(), linkedItemId, amount);
            return new AbstractEntityDto(updated);
        } else throw new AccessToResourceDenied(itemToAdd.getId());
    }

    private T itemsManipulation(T item, int action, Class itemClass, Long id, int amount) {
        PartEntity entity = new PartEntity(item.getClass().getSimpleName(),
                itemClass.getSimpleName(), id, amount);
        switch (action) {
            case 1 -> {
                return service.addToLinkedItems(item, entity);
            }
            case 0 -> {
                return service.delFromLinkedItems(item, entity);
            }
            default -> throw new IllegalArgumentException("Argument 'action' is wrong!");
        }
    }

    Showable checkItem(Long id, String type) {
        switch (type) {
            case "Document", "document" -> {
                return documentService.getById(id);
            }
            case "Fastener", "fastener" -> {
                return fastenerService.getById(id);
            }
            case "Tool", "tool" -> {
                return toolService.getById(id);
            }
            case "Consumable", "consumable" -> {
                return consumableService.getById(id);
            }
            case "Part", "part" -> {
                return partService.getById(id);
            }
            default -> throw new IllegalArgumentException("Argument 'type' is wrong!");
        }
    }
}
