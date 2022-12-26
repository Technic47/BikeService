//package ru.kuznetsov.bikeService.controllers.serviceable;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
//import org.springframework.stereotype.Component;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import ru.kuznetsov.bikeService.DAO.DAO;
//import ru.kuznetsov.bikeService.controllers.showable.DocumentController;
//import ru.kuznetsov.bikeService.controllers.usable.UsableController;
//import ru.kuznetsov.bikeService.models.Showable;
//import ru.kuznetsov.bikeService.models.bike.Serviceable;
//import ru.kuznetsov.bikeService.models.documents.Document;
//import ru.kuznetsov.bikeService.models.lists.ServiceList;
//import ru.kuznetsov.bikeService.models.service.*;
//
//import java.util.HashMap;
//import java.util.Map;
//@Component
//public class ServiceableController<T extends Serviceable & Usable> extends UsableController<T> {
//    protected DAO<ServiceList> daoServiceList;
//    protected DAO<Document> documentDAO;
//    protected DAO<Fastener> fastenerDAO;
//    protected DAO<Consumable> consumableDAO;
//    protected DAO<Tool> toolDAO;
//    protected Map<String, Showable> showableMap;
//    protected ServiceList cacheList;
//
//    public ServiceableController(DAO<T> dao, DAO<Manufacturer> daoManufacturer, DAO<ServiceList> daoServiceList) {
//        super(dao, daoManufacturer, currentObjectName, T);
//        this.daoServiceList = daoServiceList;
//        this.daoServiceList.setCurrentClass(ServiceList.class);
//        this.daoServiceList.setTableName("servicelists");
//        this.showableMap = new HashMap<>();
//    }
//
////    private void setAllDao(@Autowired DAO<Document> documentDAO, @Autowired DAO<Fastener> fastenerDAO, @Autowired DAO<Consumable> consumableDAO, @Autowired DAO<Tool> toolDAO){
////        this.documentDAO = documentDAO;
////        this.fastenerDAO = fastenerDAO;
////        this.consumableDAO = consumableDAO;
////        this.toolDAO = toolDAO;
////    }
//
//    @GetMapping("/{category}/{id}")
//    public Showable getShowable(@PathVariable("category") String category, @PathVariable("id") int id) {
//        return "forward:";
//    }
//
////    private void setTestDAO(DAO<T> daoSet){
////        this.testDao = daoSet;
////    }
//
//    @Override
//    @GetMapping("/{id}")
//    public String show(@PathVariable("id") int id, Model model) {
//        this.cacheList = dao.show(id).getServiceList();
//        for (Integer docId : cacheList.getDocsList()) {
//            this.showableMap.put("documents", getShowable("documents", docId));
//        }
//        model.addAttribute("serviceList", cacheList);
//        return super.show(id, model);
//    }
//
//    @Autowired
//    public void setDocumentDAO(DAO<Document> documentDAO) {
//        this.documentDAO = documentDAO;
//    }
//
//    @Autowired
//    public void setFastenerDAO(DAO<Fastener> fastenerDAO) {
//        this.fastenerDAO = fastenerDAO;
//    }
//
//    @Autowired
//    public void setConsumableDAO(DAO<Consumable> consumableDAO) {
//        this.consumableDAO = consumableDAO;
//    }
//
//    @Autowired
//    public void setToolDAO(DAO<Tool> toolDAO) {
//        this.toolDAO = toolDAO;
//    }
//}
