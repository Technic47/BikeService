package ru.kuznetsov.bikeService.DAO.manufactorers;

import org.springframework.jdbc.core.RowMapper;
import ru.kuznetsov.bikeService.models.service.Manufacturer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ManufacturerMapper implements RowMapper<Manufacturer> {
    @Override
    public Manufacturer mapRow(ResultSet resultSet, int i) throws SQLException {
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setId(resultSet.getInt("id"));
        manufacturer.setName(resultSet.getString("name"));
        manufacturer.setCountry(resultSet.getString("country"));
        return manufacturer;
    }
}
