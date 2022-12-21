package ru.kuznetsov.bikeService.DAO.documents;

import org.springframework.jdbc.core.RowMapper;
import ru.kuznetsov.bikeService.models.documents.Document;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DocumentMapper implements RowMapper<Document> {

    @Override
    public Document mapRow(ResultSet resultSet, int i) throws SQLException {
        Document doc = new Document();
        doc.setId(resultSet.getInt("id"));
        doc.setName(resultSet.getString("name"));
        doc.setDescription(resultSet.getString("description"));
        doc.setLink(resultSet.getString("link"));
        return doc;
    }
}
