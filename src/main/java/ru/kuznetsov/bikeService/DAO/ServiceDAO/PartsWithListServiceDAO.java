//package ru.kuznetsov.bikeService.DAO.ServiceDAO;
//
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Component;
//import ru.kuznetsov.bikeService.DAO.DAO;
//import ru.kuznetsov.bikeService.models.bike.PartsWithPartList;
//
//import java.beans.Introspector;
//import java.beans.PropertyDescriptor;
//import java.lang.reflect.Method;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//@Scope("prototype")
//public class PartsWithListServiceDAO {
//    private final PartsWithListJson cacheClass;
//    private final DAO<PartsWithListJson> dao;
//    private Map<String, Object> classProperties;
//
//    public PartsWithListServiceDAO(PartsWithPartList part, DAO<PartsWithListJson> dao) {
//        this.cacheClass = new PartsWithListJson(part.getId(), part.getManufacturer(), part.getModel(), part.getPartNumber(), part.getDescription(), part.getServiceList(), part.getPartList());
//        this.dao = dao;
//        this.dao.setCurrentClass(PartsWithListJson.class);
//    }
//
//    private Map<String, Object> getObjectProperties(final Object bean) {
//        final Map<String, Object> result = new HashMap<>();
//        try {
//            final PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(bean.getClass(), Object.class).getPropertyDescriptors();
//            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
//                final Method readMethod = propertyDescriptor.getReadMethod();
//                if (readMethod != null) {
//                    result.put(propertyDescriptor.getName(), readMethod.invoke(bean, (Object[]) null));
//                }
//            }
//        } catch (Exception ex) {
//            // ignore
//        }
//        return result;
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
//
//}
