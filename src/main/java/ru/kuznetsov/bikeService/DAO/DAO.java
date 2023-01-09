package ru.kuznetsov.bikeService.DAO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@Component
@Scope("prototype")
public class DAO<T> {
    private String tableName;
    private Class<T> currentClass;
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private StringBuilder builder;
    private StringJoiner joiner;

    @Autowired
    public DAO(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public void setCurrentClass(Class<T> currentClass) {
        this.currentClass = currentClass;
        this.tableName = this.currentClass.getSimpleName().toLowerCase() + "s";
    }

    public List<T> index() {
        builder = new StringBuilder();
        builder.append("SELECT * FROM ").append(this.tableName);
        return jdbcTemplate.query(builder.toString(), new BeanPropertyRowMapper<>(currentClass));
    }

    public T show(int id) {
        builder = new StringBuilder();
        builder.append("SELECT * FROM ").append(this.tableName).append(" WHERE id=:id");
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("id", id);
        return jdbcTemplate.queryForObject(builder.toString(), sqlParameterSource, new BeanPropertyRowMapper<>(currentClass));
    }

    public void save(T item) {
        Map<String, Object> newObjectProperties = getObjectProperties(item);
        SqlParameterSource parameterSource = new MapSqlParameterSource(newObjectProperties);
        builder = new StringBuilder();
        joiner = new StringJoiner(", ");
        StringJoiner joiner2 = new StringJoiner(", ");
        builder.append("INSERT INTO ").append(this.tableName).append(" (");

        for (Map.Entry<String, Object> str : newObjectProperties.entrySet()) {
            if (str.getKey().contains("id")) {
                joiner.add(str.getKey());
                joiner2.add("DEFAULT");
            } else {
                joiner.add(str.getKey());
                String valueItem = str.getValue() == null ? "'null'" : str.getValue().toString();
                joiner2.add("'" + valueItem + "'");
            }
        }
        builder.append(joiner);
        builder.append(") VALUES (");
        builder.append(joiner2);
        builder.append(")");
        jdbcTemplate.update(builder.toString(), parameterSource);
    }

    public void update(int id, T updateItem) {
        Map<String, Object> newObjectProperties = getObjectProperties(updateItem);
        builder = new StringBuilder();
        joiner = new StringJoiner(", ");
        builder.append("UPDATE ").append(this.tableName).append(" SET ");

        for (Map.Entry<String, Object> str : newObjectProperties.entrySet()) {
            String key = str.getKey();
            if (!key.contains("id")) {
                joiner.add(key + "= '" + str.getValue() + "'");
            }
        }
        builder.append(joiner);
        builder.append(" WHERE id=").append(id);
        jdbcTemplate.update(builder.toString(), newObjectProperties);
    }

    public void del(int id) {
        builder = new StringBuilder();
        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("id", id);
        builder.append("DELETE FROM ").append(this.tableName).append(" WHERE id=").append(id);
        jdbcTemplate.update(builder.toString(), sqlParameterSource);
    }

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
}
