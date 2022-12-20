package ru.kuznetsov.bikeService.DAO.documents;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.kuznetsov.bikeService.models.documents.Document;

import java.util.List;

@Component
public class DocumentsDAO {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DocumentsDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Document> index() {
        return jdbcTemplate.query("SELECT * FROM documents", new DocumentMapper());
    }

    public Document show(int id) {
        return jdbcTemplate.query("SELECT * FROM documents WHERE docid=?",
                        new Object[]{id}, new DocumentMapper())
                .stream().findAny().orElse(null);
    }

    public void save(Document doc) {
        jdbcTemplate.update("INSERT INTO documents VALUES(DEFAULT, ?, ?, ?)",
                doc.getName(), doc.getDescription(), doc.getLink());
    }

    public void update(int id, Document updateDoc) {
        jdbcTemplate.update("UPDATE documents SET name=?, description=?, link=? WHERE docid=?",
                updateDoc.getName(), updateDoc.getDescription(), updateDoc.getLink(), updateDoc.getId());
    }

    public void del(int id) {
        jdbcTemplate.update("DELETE FROM documents WHERE docid=?", id);
    }
}
