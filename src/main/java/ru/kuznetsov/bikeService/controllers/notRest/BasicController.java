package ru.kuznetsov.bikeService.controllers.notRest;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.kuznetsov.bikeService.controllers.notRest.abstracts.AbstractController;
import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;
import ru.kuznetsov.bikeService.models.lists.PartEntity;
import ru.kuznetsov.bikeService.models.lists.UserEntity;
import ru.kuznetsov.bikeService.models.pictures.Picture;
import ru.kuznetsov.bikeService.models.servicable.Bike;
import ru.kuznetsov.bikeService.models.servicable.Part;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.services.PDFService;
import ru.kuznetsov.bikeService.services.abstracts.CommonAbstractEntityService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import static ru.kuznetsov.bikeService.models.users.UserRole.ROLE_ADMIN;
import static ru.kuznetsov.bikeService.models.users.UserRole.ROLE_USER;
import static ru.kuznetsov.bikeService.services.PDFService.PDF_DOC_NAME;

@Component
public abstract class BasicController<T extends AbstractShowableEntity,
        S extends CommonAbstractEntityService<T>>
        extends AbstractController {
    protected final S service;
    protected T thisClassNewObject;
    protected String category;
    protected PDFService pdfService;


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
        List<T> objects = null;
        UserModel userModel = this.getUserModelFromPrincipal(principal);
        Long userId = userModel.getId();

        if (userModel.getAuthorities().contains(ROLE_USER)) {
            objects = service.findByCreatorOrShared(userId);
            logger.info("personal " + category + " are shown to '" + userModel.getUsername() + "'");
        }
        if (userModel.getAuthorities().contains(ROLE_ADMIN)) {
            objects = service.index();
            logger.info(category + " are shown to " + userModel.getUsername());
        }

        Map<T, String> indexMap = new HashMap<>();
        Map<T, String> sharedIndexMap = new HashMap<>();
        if (objects != null) {
            objects.forEach(object -> {
                if (object.getCreator().equals(userId)) {
                    indexMap.put(object, pictureService.getById(object.getPicture()).getName());
                } else sharedIndexMap.put(object, pictureService.getById(object.getPicture()).getName());
            });
        }
        model.addAttribute("objects", indexMap);
        model.addAttribute("sharedObjects", sharedIndexMap);
        model.addAttribute("sharedCheck", false);
        this.addItemAttributesIndex(model, principal);
        return "index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") Long id,
                       Model model, Principal principal) {
        T item = service.getById(id);
        if (item == null) {
            return "redirect:/" + category;
        } else
            return this.show(item, model, principal);
    }

    protected String show(T item, Model model, Principal principal) {
        boolean access = checkAccessToItem(item, principal);
        model.addAttribute("picture", pictureService.getById(item.getPicture()).getName());
        this.addItemAttributesShow(model, item, principal);
        model.addAttribute("access", access);
        logger.info(category + " " + item.getId() + " was shown to '" + this.getUserModelFromPrincipal(principal).getUsername() + "'");
        return "show";
    }

    private boolean checkAccessToItem(T item, Principal principal) {
        UserModel userModel = this.getUserModelFromPrincipal(principal);
        if (userModel.getAuthorities().contains(ROLE_ADMIN)) {
            return true;
        } else {
            return item.getCreator().equals(userModel.getId());
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

    private void addItemAttributesShow(Model model, T item, Principal principal) {
        model.addAttribute("object", item);
        switch (category) {
            case "parts", "bikes" -> model.addAttribute("type", "Serviceable");
            case "tools", "consumables" -> model.addAttribute("type", "Usable");
            case "documents", "fasteners", "manufacturers" -> model.addAttribute("type", "Showable");
        }
        this.addItemAttributesIndex(model, principal);
    }

    protected void addItemAttributesNew(Model model, T item, Principal principal) {
        model.addAttribute("allPictures", pictureService.index());
        this.addItemAttributesShow(model, item, principal);
    }

    protected void addItemAttributesEdit(Model model, T item, Principal principal) {
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
        if (!file.isEmpty()) {
            Picture picture = pictureService.save(file);
            item.setPicture(picture.getId());
        }
        UserModel userModel = this.getUserModelFromPrincipal(principal);
        item.setCreator(userModel.getId());
        item.setCreated(new Date());
        userService.addCreatedItem(userModel,
                new UserEntity(thisClassNewObject.getClass().getSimpleName(), service.save(item).getId()));
        logger.info(item + " was created by '" + userModel.getUsername());
        return "redirect:/" + category;
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") Long id, Principal principal) {
        T item = service.getById(id);
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
            if (!file.isEmpty()) {
                Picture picture = pictureService.save(file);
                newItem.setPicture(picture.getId());
            }
            service.update(oldItem, newItem);
            UserModel userModel = this.getUserModelFromPrincipal(principal);
            logger.info(newItem.getClass()
                    .getSimpleName() + " id:" + oldItem.getId() + " was edited by '" + userModel.getUsername() + "'");
            return "redirect:/" + category;
        } else return "redirect:/" + category + "/" + oldItem.getId();
    }

    @PostMapping(value = "/{id}")
    public String delete(@PathVariable("id") Long id, Principal principal) {
        T item = service.getById(id);
        if (checkAccessToItem(item, principal)) {
            UserModel userModel = this.getUserModelFromPrincipal(principal);
            service.delete(id);
            this.cleanUpAfterDelete(item, id, userModel);
            logger.info(category +
                    " id:" + id + " was deleted by '" + userModel.getUsername() + "'");
            return "redirect:/" + category;
        } else return "redirect:/" + category + "/" + id;
    }

    private void cleanUpAfterDelete(T item, Long id, UserModel userModel) {
        String partType = item.getClass().getSimpleName();
        Runnable clearUser = () -> userService
                .delCreatedItem(userModel, new UserEntity(thisClassNewObject.getClass().getSimpleName(), id));
        Runnable clearParts = () -> {
            PartEntity entityPart = new PartEntity("Part", partType, id, 1);
            List<Part> listOfParts = this.partService.findByLinkedItemsItemIdAndLinkedItemsType(id, partType);
            listOfParts.parallelStream().forEach(part -> partService.delFromLinkedItems(part, entityPart));
        };
        Runnable clearBikes = () -> {
            PartEntity entityBike = new PartEntity("Bike", partType, id, 1);
            List<Bike> listOfBikes = this.bikeService.findByLinkedPartsItemIdAndLinkedPartsType(id, partType);
            listOfBikes.parallelStream().forEach(part -> bikeService.delFromLinkedItems(part, entityBike));
        };
        mainExecutor.submit(clearUser);
        mainExecutor.submit(clearParts);
        mainExecutor.submit(clearBikes);
    }

    @GetMapping(value = "/pdf")
    @ResponseBody
    public ResponseEntity<Resource> createPdf(@Param("id") Long id, Principal principal) throws IOException {
        T item = this.service.getById(id);

        return this.prepareResponse(item, principal);
    }

    protected ResponseEntity<Resource> prepareResponse(T item, Principal principal) throws IOException {
        this.preparePDF(item, principal);
        return this.createResponse(item);
    }

    protected ResponseEntity<Resource> createResponse(T item) throws IOException {
        File file = new File(PDF_DOC_NAME);
        Path path = Paths.get(file.getAbsolutePath());
        ByteArrayResource resource = new ByteArrayResource
                (Files.readAllBytes(path));

        return ResponseEntity.ok().headers(this.headers(item.getClass().getSimpleName() + "_" + item.getId()))
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType
                        ("application/octet-stream")).body(resource);
    }

    private HttpHeaders headers(String fileName) {
        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=" + fileName + ".pdf");
        header.add("Cache-Control", "no-cache, no-store,"
                + " must-revalidate");
        header.add("Pragma", "no-cache");
        header.add("Expires", "0");
        return header;
    }

    protected void preparePDF(T item, Principal principal) {
        UserModel userModel = this.getUserModelFromPrincipal(principal);
        this.pdfService.newPDFDocument()
                .addUserName(userModel.getUsername())
                .addImage(this.pictureService.getById(item.getPicture()).getName())
                .buildShowable(item);
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
        Set<T> resultSet = new HashSet<>();
        resultSet.addAll(this.service.findByNameContainingIgnoreCase(value));
        resultSet.addAll(this.service.findByDescriptionContainingIgnoreCase(value));

        UserModel userModel = this.getUserModelFromPrincipal(principal);
        Long userId = userModel.getId();

        if (!userModel.getAuthorities().contains(ROLE_ADMIN)) {
            if (shared) {
                resultSet = resultSet.stream()
                        .filter(item -> item.getCreator().equals(userId)
                                || item.getIsShared())
                        .collect(Collectors.toSet());
            } else {
                resultSet = resultSet.stream()
                        .filter(item -> item.getCreator().equals(userId))
                        .collect(Collectors.toSet());
            }
        }

        Map<T, String> resultMap = new HashMap<>();
        Map<T, String> sharedIndexMap = new HashMap<>();
        resultSet.forEach(object -> {
            if (object.getCreator().equals(userId)) {
                resultMap.put(object, pictureService.getById(object.getPicture()).getName());
            } else sharedIndexMap.put(object, pictureService.getById(object.getPicture()).getName());
        });

        model.addAttribute("user", userModel);
        model.addAttribute("objects", resultMap);
        model.addAttribute("sharedObjects", sharedIndexMap);
        model.addAttribute("category", category);

        logger.info(userModel.getUsername() + " was searching " + value + " in " + category);
        return "index";
    }

    @Autowired
    public void setPdfService(PDFService pdfService) {
        this.pdfService = pdfService;
    }
}
