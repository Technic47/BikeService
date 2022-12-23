//package ru.kuznetsov.bikeService.DAO.ServiceDAO;
//
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import ru.kuznetsov.bikeService.models.lists.ServiceList;
//import ru.kuznetsov.bikeService.models.service.Manufacturer;
//
//import java.util.Map;
//
//public class PartsWithListJson {
//    private int id;
//    private String manufacturer;
//    private String model;
//    private String partNumber;
//    private String description;
//    private String serviceList;
//    private String partList;
//
//    public PartsWithListJson(int id, Manufacturer manufacturer, String model, String partNumber, String description, ServiceList serviceList, Map<String, Integer> partList) {
//        this.id = id;
//        this.manufacturer = this.objectToJson(manufacturer);
//        this.model = model;
//        this.partNumber = partNumber;
//        this.description = description;
//        this.serviceList = this.objectToJson(serviceList);
//        this.partList = this.objectToJson(partList);
//    }
//
//    public String objectToJson(Object obj) {
//        Gson gson = new Gson();
//        return gson.toJson(obj, new TypeToken<Map<String, Integer>>() {
//        }.getType());
//    }
//
//    public Map<String, Integer> jsonToObject(String json) {
//        Gson gson = new Gson();
//        return gson.fromJson(json, new TypeToken<Map<String, Integer>>() {
//        }.getType());
//    }
//}
