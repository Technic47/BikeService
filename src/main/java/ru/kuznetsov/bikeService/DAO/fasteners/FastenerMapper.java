package ru.kuznetsov.bikeService.DAO.fasteners;

import org.springframework.jdbc.core.RowMapper;
import ru.kuznetsov.bikeService.models.service.Fastener;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FastenerMapper implements RowMapper<Fastener> {
    @Override
    public Fastener mapRow(ResultSet resultSet, int i) throws SQLException {
        Fastener fastener = new Fastener();
        fastener.setFastenerid(resultSet.getInt("fastenerid"));
        fastener.setType(resultSet.getString("type"));
        fastener.setSpecs(resultSet.getString("specs"));
        fastener.setDescription(resultSet.getString("description"));
        return fastener;
    }
}
