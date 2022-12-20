package ru.kuznetsov.bikeService.DAO.manufactorers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.kuznetsov.bikeService.DAO.fasteners.FastenerMapper;
import ru.kuznetsov.bikeService.models.service.Fastener;
import ru.kuznetsov.bikeService.models.service.Manufacturer;

import java.util.List;

@Component
public class ManufacturerDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ManufacturerDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Manufacturer> index() {
        return jdbcTemplate.query("SELECT * FROM manufacturers", new ManufacturerMapper());
    }

    public Manufacturer show(int id) {
        return jdbcTemplate.query("SELECT * FROM manufacturers WHERE manufacturerid=?",
                        new Object[]{id}, new ManufacturerMapper())
                .stream().findAny().orElse(null);
    }

    public void save(Manufacturer item) {
        jdbcTemplate.update("INSERT INTO manufacturers VALUES(?, ?,DEFAULT)",
                item.getName(), item.getCountry());
    }

    public void update(int id, Manufacturer updateItem) {
        jdbcTemplate.update("UPDATE manufacturers SET name=?, country=? WHERE manufacturerid=?",
                updateItem.getName(), updateItem.getCountry(), updateItem.getManufacturerid());
    }

    public void del(int id) {
        jdbcTemplate.update("DELETE FROM fasteners WHERE fastenerid=?", id);
    }

}
