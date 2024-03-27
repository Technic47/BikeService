package ru.bikeservice.controllers.notRest;

import jakarta.validation.Valid;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.bikeservice.mainresources.controllers.abstracts.AbstractEntityController;
import ru.bikeservice.mainresources.models.abstracts.AbstractShowableEntity;
import ru.bikeservice.mainresources.models.users.UserModel;
import ru.bikeservice.mainresources.services.abstracts.CommonAbstractEntityService;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;


/**
 * Abstract non-REST controller that provides general methods and mapping for AbstractShowableEntity models.
 *
 * @param <T> entities from AbstractShowableEntity.
 */
@Component
public abstract class BasicController<T extends AbstractShowableEntity,
        S extends CommonAbstractEntityService<T>>
        extends AbstractEntityController {
    protected final S service;
    protected T thisClassNewObject;
    protected String category;


    public BasicController(S service) {
        this.service = service;
    }

    public void setCurrentClass(Class<T> currentClass) {
        this.category = currentClass.getSimpleName().toLowerCase() + "s";
        try {
            this.thisClassNewObject = currentClass.getConstructor().newInstance();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    @GetMapping()
    public String index(Model model, Principal principal) {
        UserModel userModel = this.getUserModelFromPrincipal(principal);
        Long userId = userModel.getId();
        List<T> objects = this.doIndexProcedure(service, userModel, category, true);

        this.addIndexMapsToModel(model, userId, objects);
        model.addAttribute("sharedCheck", true);
        this.addItemAttributesIndex(model, principal);
        return "index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id,
                       Model model, Principal principal) {
        T item = service.getById(id);
        if (item == null) {
            return "redirect:/" + category;
        } else {
            item = this.doShowProcedure(item, principal);
            return this.show(item, model, principal, category);
        }
    }

    String show(T item, Model model, Principal principal, String category) {
        if (item == null) {
            return "redirect:/" + category;
        } else {
            boolean access = checkAccessToItem(item, principal);
            this.addItemAttributesShow(model, item, principal);
            model.addAttribute("picture", pictureService.getById(item.getPicture()).getName());
            model.addAttribute("access", access);

            return "show";
        }
    }

    @GetMapping(value = "/new")
    public String newItem(Model model, Principal principal) {
        this.addItemAttributesNew(model, thisClassNewObject, principal);
        return "new";
    }

    private void addItemAttributesIndex(Model model, Principal principal) {
        model.addAttribute("category", category);
        this.addUserToModel(model, principal);
    }

    void addItemAttributesShow(Model model, T item, Principal principal) {
        model.addAttribute("object", item);
        switch (category) {
            case "parts", "bikes" -> model.addAttribute("type", "Serviceable");
            case "tools", "consumables" -> model.addAttribute("type", "Usable");
            case "documents", "fasteners", "manufacturers" -> model.addAttribute("type", "Showable");
        }
        this.addItemAttributesIndex(model, principal);
    }

    void addItemAttributesNew(Model model, T item, Principal principal) {
        model.addAttribute("allPictures", pictureService.getAll());
        this.addItemAttributesShow(model, item, principal);
    }

    void addItemAttributesEdit(Model model, T item, Principal principal) {
        model.addAttribute("picture", pictureService.getById(item.getPicture()));
        this.addItemAttributesNew(model, item, principal);
    }

    @PostMapping()
    public String create(@Valid @ModelAttribute("object") T item,
                         BindingResult bindingResult,
                         @RequestPart("newImage") MultipartFile file,
                         Model model, Principal principal
    ) {
        if (bindingResult.hasErrors()) {
            this.addItemAttributesNew(model, item, principal);
            return "new";
        }
        this.doCreateProcedure(item, service, file, principal);
        return "redirect:/" + category;
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id, Principal principal) {
        T item = service.getById(id);
        item = this.doShowProcedure(item, principal);
        if (item == null) {
            return "redirect:/" + category;
        } else
            return this.edit(model, item, principal);
    }

    protected String edit(Model model, T item, Principal principal) {
        if (checkAccessToItem(item, principal)) {
            this.addItemAttributesEdit(model, item, principal);

            if (Objects.equals(category, "parts") || Objects.equals(category, "bikes")) {
                return "editPart";
            }
            return "edit";
        } else return "redirect:/" + category + "/" + item.getId();
    }

    @PostMapping("/{id}/edit")
    public String update(@Valid @ModelAttribute("object") T newItem,
                         BindingResult bindingResult,
                         @RequestPart(value = "newImage") MultipartFile file,
                         @PathVariable("id") Long id,
                         Model model, Principal principal) {
        T item = service.getById(id);
        return this.update(newItem, bindingResult, file, item, model, principal);
    }

    public String update(T newItem, BindingResult bindingResult, MultipartFile file, T oldItem, Model model, Principal principal) {
        if (checkAccessToItem(oldItem, principal)) {
            if (bindingResult.hasErrors()) {
                this.addItemAttributesEdit(model, newItem, principal);
                if (Objects.equals(category, "parts") || Objects.equals(category, "bikes")) {
                    return "editPart";
                }
                return "edit";
            }

            this.doUpdateProcedure(newItem, service, oldItem, file, principal);

            return "redirect:/" + category;
        } else return "redirect:/" + category + "/" + oldItem.getId();
    }

    @PostMapping(value = "/{id}")
    public String delete(@PathVariable("id") Long id, Principal principal) {
        T item = service.getById(id);
        if (checkAccessToItem(item, principal)) {
            this.doDeleteProcedure(item, service, principal);
            return "redirect:/" + category;
        } else return "redirect:/" + category + "/" + id;
    }

    @GetMapping(value = "/pdf")
    @ResponseBody
    public ResponseEntity<Resource> createPdf(@Param("id") Long id, Principal principal) throws IOException {
        T item = this.service.getById(id);
        return this.prepareShowablePDF(item, principal);
    }

    /**
     * Searching via matching in Name and Description. Case is ignored. ResultSet is formed considering current user`s ROLE.
     * If user is ADMIN -> no filtering.
     * Else -> filtering remains shared and ID-filtered items, or just ID-filtered items.
     *
     * @param value  string to search.
     * @param shared flag for including shared items to resultSet.
     * @param model
     * @return "index" page with search results.
     */
    @GetMapping(value = "/search")
    public String search(@RequestParam(value = "value") String value,
                         @RequestParam(value = "shared", required = false) boolean shared,
                         Model model,
                         Principal principal) {
        UserModel userModel = this.getUserModelFromPrincipal(principal);
        Long userId = userModel.getId();

        List<T> resultList = null;
        try {
            resultList = (List<T>) this.searchService.doSearchProcedure("standard", value, userModel, shared, category);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        Map<T, String> resultMap = new HashMap<>();
        Map<T, String> sharedIndexMap = new HashMap<>();
        resultList.forEach(object -> {
            if (object.getCreator().equals(userId)) {
                resultMap.put(object, pictureService.getById(object.getPicture()).getName());
            } else sharedIndexMap.put(object, pictureService.getById(object.getPicture()).getName());
        });

        model.addAttribute("user", userModel);
        model.addAttribute("objects", resultMap);
        model.addAttribute("sharedObjects", sharedIndexMap);
        model.addAttribute("category", category);

        return "index";
    }
}