package ru.kuznetsov.bikeService.DAO.factory;

import org.springframework.context.annotation.Bean;
import ru.kuznetsov.bikeService.DAO.DAORepository;
import ru.kuznetsov.bikeService.models.Picture;
import ru.kuznetsov.bikeService.models.bike.Bike;
import ru.kuznetsov.bikeService.models.bike.Part;
import ru.kuznetsov.bikeService.models.documents.Document;
import ru.kuznetsov.bikeService.models.service.Consumable;
import ru.kuznetsov.bikeService.models.service.Fastener;
import ru.kuznetsov.bikeService.models.service.Manufacturer;
import ru.kuznetsov.bikeService.models.service.Tool;

public class DAOFactory {
    @Bean
    public static DAORepository<Document> getDocumentDAO() {
        return new DAORepository<Document>();
    }

    @Bean
    public static DAORepository<Consumable> getConsumableDAO() {
        return new DAORepository<Consumable>();
    }

    @Bean
    public static DAORepository<Fastener> getFastenerDAO() {
        return new DAORepository<Fastener>();
    }

    @Bean
    public static DAORepository<Manufacturer> getManufacturerDAO() {
        return new DAORepository<Manufacturer>();
    }

    @Bean
    public static DAORepository<Tool> getToolDAO() {
        return new DAORepository<Tool>();
    }

    @Bean
    public static DAORepository<Part> getPartDAO() {
        return new DAORepository<Part>();
    }

    @Bean
    public static DAORepository<Bike> getBikeDAO() {
        return new DAORepository<Bike>();
    }

    @Bean
    public static DAORepository<Picture> getPictureDAO() {
        return new DAORepository<Picture>();
    }
}
