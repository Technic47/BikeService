//package ru.kuznetsov.bikeService.DAO;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Scope;
//import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
//import org.springframework.stereotype.Component;
//import ru.kuznetsov.bikeService.models.documents.Document;
//
//@Component
//@Scope("prototype")
//public class DAOBeans {
//
//    private final NamedParameterJdbcTemplate jdbcTemplate;
//
//    @Autowired
//    public DAOBeans(NamedParameterJdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    @Bean
//    public DAO<Document> getDocumentDAO() {
//        DAO<Document> documentDAO = new DAO<>(jdbcTemplate);
//        documentDAO.setCurrentClass(Document.class);
//        return documentDAO;
//    }
//}
