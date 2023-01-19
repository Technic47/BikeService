package ru.kuznetsov.bikeService.repositories.services;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface CommonInterface<T> {
    public void save(T entity);

    public T show(Long id);

    public void update(Long id, T updateItem);

    public List<T> index();

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
