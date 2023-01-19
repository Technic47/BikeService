package ru.kuznetsov.bikeService.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import ru.kuznetsov.bikeService.models.abstracts.BaseEntity;
import ru.kuznetsov.bikeService.repositories.CommonRepository;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Scope("prototype")
public class DAORepository<T extends BaseEntity> extends DAO<T> {
    private CommonRepository<T> repository;

    @Override
    public void save(T entity) {
        repository.save(entity);
    }

    @Override
    public T show(Long id) {
        return repository.findById(id).get();
    }

    @Override
    public void update(Long id, T updateItem) {
        T newItem = show(id);
    }

    @Override
    public List<T> index() {
        return new ArrayList<>(repository.findAll());
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Map<String, Object> getObjectProperties(final Object bean) {
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

    @Autowired
    public void setRepository(CommonRepository<T> repository) {
        this.repository = repository;
    }
}
