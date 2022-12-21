package ru.kuznetsov.bikeService.DAO.fasteners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.kuznetsov.bikeService.models.service.Fastener;

import java.util.List;

@Component
public class FastenerDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FastenerDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Fastener> index() {
        return jdbcTemplate.query("SELECT * FROM fasteners", new BeanPropertyRowMapper<>(Fastener.class));
    }

    public Fastener show(int id) {
        return jdbcTemplate.query("SELECT * FROM fasteners WHERE id=?",
                        new Object[]{id}, new BeanPropertyRowMapper<>(Fastener.class))
                .stream().findAny().orElse(null);
    }

    public void save(Fastener item) {
        jdbcTemplate.update("INSERT INTO fasteners VALUES(?, ?, ?, DEFAULT)",
                item.getType(), item.getSpecs(), item.getDescription());
    }

    public void update(int id, Fastener updateItem) {
        jdbcTemplate.update("UPDATE fasteners SET type=?, description=?, specs=? WHERE id=?",
                updateItem.getType(), updateItem.getDescription(), updateItem.getSpecs(), id);
    }

    public void del(int id) {
        jdbcTemplate.update("DELETE FROM fasteners WHERE id=?", id);
    }
}
