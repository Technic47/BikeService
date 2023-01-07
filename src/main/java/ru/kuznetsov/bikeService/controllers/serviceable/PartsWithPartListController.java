package ru.kuznetsov.bikeService.controllers.serviceable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kuznetsov.bikeService.DAO.DAO;
import ru.kuznetsov.bikeService.models.bike.*;
import ru.kuznetsov.bikeService.models.service.Usable;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class PartsWithPartListController<T extends ServiceableWithParts & Serviceable & Usable> extends ServiceableController<T> {

    private DAO<SmallPart> smallPartDAO;
    private DAO<Part> partDAO;
    private DAO<Unit> unitDAO;
    private DAO<Bike> bikeDAO;
    private Map<String, List<Integer>> cachePartList;

    @Override
    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        this.updateCachePartList(id);
        this.addItemServiceableToModel(model);
        return super.show(id, model);
    }

    @Override
    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        this.updateCachePartList(id);
        this.addItemServiceableToModel(model);
        this.addAllServicableToModel(model);
        return super.edit(model, id);
    }

    @RequestMapping(value = "/{id}/update/serviceList")
    public String updatePartList(@Valid T item, BindingResult bindingResult,
                                 @PathVariable("id") int id,
                                 @RequestParam(value = "action") String action,
                                 @RequestParam(value = "smallpartId") int smallpartId,
                                 @RequestParam(value = "partId") int partId,
                                 @RequestParam(value = "unitId") int unitId,
                                 Model model){
        switch (action) {
            case "finish":
                return this.update(item, bindingResult, id);
            case "addSmallpart":
                item.addToPartList(smallPartDAO.show(smallpartId));
                break;
            case "delSmallpart":
                item.delFromPartList(smallPartDAO.show(smallpartId));
                break;
            case "addPart":
                item.addToPartList(partDAO.show(partId));
                break;
            case "delPart":
                item.delFromPartList(partDAO.show(partId));
                break;
            case "addUnit":
                item.addToPartList(unitDAO.show(unitId));
                break;
            case "delUnit":
                item.delFromPartList(unitDAO.show(unitId));
                break;
        }
        dao.update(id, item);
        return edit(model, id);
    }

    private void updateCachePartList(int id) {
        this.cachePartList = dao.show(id).returnPartListObject();
    }

    private void addItemServiceableToModel(Model model) {
        model.addAttribute("smallparts", this.getObjectSmallParts());
        model.addAttribute("parts", this.getObjectParts());
        model.addAttribute("units", this.getObjectUnits());
    }

    private void addAllServicableToModel(Model model){
        model.addAttribute("allSmallparts", smallPartDAO.index());
        model.addAttribute("allParts", partDAO.index());
        model.addAttribute("allUnits", unitDAO.index());
    }

    private List<SmallPart> getObjectSmallParts() {
        List<SmallPart> smallPartsList = new ArrayList<>();
        for (Integer id : cachePartList.get("smallPart")) {
            smallPartsList.add(smallPartDAO.show(id));
        }
        return smallPartsList;
    }

    private List<Part> getObjectParts() {
        List<Part> smallPartsList = new ArrayList<>();
        for (Integer id : cachePartList.get("Part")) {
            smallPartsList.add(partDAO.show(id));
        }
        return smallPartsList;
    }

    private List<Unit> getObjectUnits() {
        List<Unit> smallPartsList = new ArrayList<>();
        for (Integer id : cachePartList.get("Unit")) {
            smallPartsList.add(unitDAO.show(id));
        }
        return smallPartsList;
    }

    public PartsWithPartListController(DAO<T> dao) {
        super(dao);
    }

    @Autowired
    public void setSmallPartDAO(DAO<SmallPart> smallPartDAO) {
        this.smallPartDAO = smallPartDAO;
        this.smallPartDAO.setCurrentClass(SmallPart.class);
        this.smallPartDAO.setTableName("smallparts");
    }

    @Autowired
    public void setPartDAO(DAO<Part> partDAO) {
        this.partDAO = partDAO;
        this.partDAO.setCurrentClass(Part.class);
        this.partDAO.setTableName("parts");
    }

    @Autowired
    public void setUnitDAO(DAO<Unit> unitDAO) {
        this.unitDAO = unitDAO;
        this.unitDAO.setCurrentClass(Unit.class);
        this.unitDAO.setTableName("units");
    }

    @Autowired
    public void setBikeDAO(DAO<Bike> bikeDAO) {
        this.bikeDAO = bikeDAO;
        this.bikeDAO.setCurrentClass(Bike.class);
        this.bikeDAO.setTableName("bikes");
    }
}
