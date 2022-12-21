package ru.kuznetsov.bikeService.DAO;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class DAO<T> {
    private T t;
    private String tableName;
    private final Class<T> currentClass;
    private final JdbcTemplate jdbcTemplate;
    private final StringBuilder builder = new StringBuilder();

    public DAO(Class<T> currentClass, JdbcTemplate jdbcTemplate) {
        this.currentClass = currentClass;
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<T> index() {
        builder.append("SELECT * FROM ").append(this.tableName);
        return jdbcTemplate.query(builder.toString(), new BeanPropertyRowMapper<>(currentClass));
    }

    public T show(int id) {
        builder.append("SELECT * FROM ").append(this.tableName).append("WHERE id=?");
        return jdbcTemplate.query(builder.toString(), new Object[]{id}, new BeanPropertyRowMapper<>(currentClass))
                .stream().findAny().orElse(null);
    }

    public void del(int id) {
        builder.append("DELETE FROM ").append(this.tableName).append("WHERE id=?");
        jdbcTemplate.update(builder.toString(), id);
    }
}
