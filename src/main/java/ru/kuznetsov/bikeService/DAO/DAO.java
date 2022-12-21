package ru.kuznetsov.bikeService.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DAO<T> {
    private String tableName;
    private Class<T> currentClass;
    private final JdbcTemplate jdbcTemplate;
    private StringBuilder builder;

    @Autowired
    public DAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setCurrentClass(Class<T> currentClass) {
        this.currentClass = currentClass;
    }

    public List<T> index() {
        builder = new StringBuilder();
        builder.append("SELECT * FROM ").append(this.tableName);
        return jdbcTemplate.query(builder.toString(), new BeanPropertyRowMapper<>(currentClass));
    }

    public T show(int id) {
        builder = new StringBuilder();
        builder.append("SELECT * FROM ").append(this.tableName).append(" WHERE id=?");
        return jdbcTemplate.query(builder.toString(), new Object[]{id}, new BeanPropertyRowMapper<>(currentClass))
                .stream().findAny().orElse(null);
    }

    public void save(T item) {
        Map<String, Object> newObjectProperties = getObjectProperties(item);
        builder = new StringBuilder();
//        "INSERT INTO documents VALUES(DEFAULT, ?, ?, ?)"
        builder.append("INSERT INTO ").append(this.tableName).append(" VALUES(");
        int count = 0;
        List<Object> objectsList = new ArrayList<>();
        for (Map.Entry<String, Object> str : newObjectProperties.entrySet()) {
            if (str.getKey().contains("id")) {
                builder.append("DEFAULT");
            } else {
                builder.append("?");
            }
            if (count != newObjectProperties.keySet().size()) {
                builder.append(", ");
            }

            objectsList.add(str.getValue());
            count++;
        }
        builder.append(")");
        jdbcTemplate.update(builder.toString(), objectsList);
    }

    public void update(int id, T updateItem) {
//        "UPDATE documents SET name=?, description=?, link=? WHERE id=?"
        Map<String, Object> newObjectProperties = getObjectProperties(updateItem);
        builder = new StringBuilder();
        builder.append("UPDATE ").append(this.tableName).append(" SET ");
        int count = 0;
        int size = newObjectProperties.keySet().size();
        List<Object> objectsList = new ArrayList<>(size);
        for (Map.Entry<String, Object> str : newObjectProperties.entrySet()) {
            String key = str.getKey();
            if (!key.contains("id")) {
                builder.append(key).append("=?");
                objectsList.add(str.getValue());
            } else {
                objectsList.add(size - 1, id);
            }
            if (count != size) {
                builder.append(", ");
            }
            count++;
        }
        builder.append("WHERE id=?");
        jdbcTemplate.update(builder.toString(), objectsList);
    }

    public void del(int id) {
        builder = new StringBuilder();
        builder.append("DELETE FROM ").append(this.tableName).append("WHERE id=?");
        jdbcTemplate.update(builder.toString(), id);
    }

    private Map<String, Object> getObjectProperties(final Object bean) {
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

        return result;
    }


}
