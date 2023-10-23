package ru.bikeservice.mainresources.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.kafka.annotation.EnableKafka;

import javax.sql.DataSource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@EnableKafka
@Configuration
public class Config {
    public static String UPLOAD_PATH;
    @Value("${upload.path}")
    private String path;
    @Value("${datasource.driver}")
    private String driver;
    @Value("${spring.datasource.url}")
    private String jdbcURL;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    @Value("${upload.path}")
    private void setUploadPath() {
        UPLOAD_PATH = this.path;
    }

    @Bean(name = "MainExecutor")
    public ExecutorService getMainService() {
        int coreCount = Runtime.getRuntime().availableProcessors();
        return Executors.newFixedThreadPool(coreCount);
    }

    @Bean(name = "AdditionExecutor")
    public ExecutorService getAdditionService() {
        return Executors.newFixedThreadPool(2);
    }

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(jdbcURL);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        return dataSource;
    }

//    @Bean
//    public DocumentService getDocumentService(DocumentRepository repository){
//        return new DocumentService(repository);
//    }
//
//    @Bean
//    public FastenerService getFastenerService(FastenerRepository repository){
//        return new FastenerService(repository);
//    }
//
//    @Bean
//    public ManufacturerService getManufacturerService(ManufacturerRepository repository){
//        return new ManufacturerService(repository);
//    }
//
//    @Bean
//    public ConsumableService getConsumableService(ConsumableRepository repository){
//        return new ConsumableService(repository);
//    }
//
//    @Bean
//    public ToolService getToolService(ToolRepository repository){
//        return new ToolService(repository);
//    }
//
//    @Bean
//    public PartService getPartService(PartRepository repository){
//        return new PartService(repository);
//    }
//
//    @Bean
//    public BikeService getBikeService(BikeRepository repository){
//        return new BikeService(repository);
//    }
}
