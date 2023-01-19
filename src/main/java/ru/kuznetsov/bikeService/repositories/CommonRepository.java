package ru.kuznetsov.bikeService.repositories;

import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.kuznetsov.bikeService.models.abstracts.BaseEntity;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

//@NoRepositoryBean

@Scope("prototype")
@Repository
public interface CommonRepository<T extends BaseEntity> extends JpaRepository<T, Long> {
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
