package ru.kuznetsov.bikeService.services.abstracts;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CommonService<E> {
    public E save(E entity);

    public E show(Long id);

    public void update(Long id, E updateItem);

    public List<E> index();

    public void delete(Long id);

    public default Map<String, Object> getObjectProperties(final Object bean) {
        final Map<String, Object> result = new HashMap<>();
        try {
            final PropertyDescriptor[] propertyDescriptors = Introspector.getBeanInfo(bean.getClass(), Object.class).getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                final Method readMethod = propertyDescriptor.getReadMethod();
                if (readMethod != null) {
                    result.put(propertyDescriptor.getName(), readMethod.invoke(bean, (Object[]) null));
                }
            }
        } catch (Exception ex) {
            // ignore
        }
        result.remove("value");
        return result;
    }
}
