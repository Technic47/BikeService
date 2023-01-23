//package ru.kuznetsov.bikeService.DAO;
//
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Component;
//import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;
//import ru.kuznetsov.bikeService.repositories.abstracts.CommonRepository;
//
//import java.beans.Introspector;
//import java.beans.PropertyDescriptor;
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@Component
//@Scope("prototype")
//public class DAORepository<T extends AbstractShowableEntity>{
//    private CommonRepository<T> repository;
//
//
//    public void save(T entity) {
//        repository.save(entity);
//    }
//
//
//    public T show(Long id) {
//        return repository.findById(id).get();
//    }
//
//
//    public void update(Long id, T updateItem) {
//        T newItem = show(id);
//    }
//
//
//    public List<T> index() {
//        return new ArrayList<>(repository.findAll());
//    }
//
//
//    public void delete(Long id) {
//        repository.deleteById(id);
//    }
//
//
//    public Map<String, Object> getObjectProperties(final Object bean) {
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
//        result.remove("value");
//        return result;
//    }
//
//
//    public void setRepository(CommonRepository<T> repository) {
//        this.repository = repository;
//    }
//}
