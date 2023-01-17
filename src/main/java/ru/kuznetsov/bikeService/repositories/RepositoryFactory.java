//package ru.kuznetsov.bikeService.repositories;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//import ru.kuznetsov.bikeService.models.Showable;
//import ru.kuznetsov.bikeService.models.bike.Serviceable;
//import ru.kuznetsov.bikeService.models.service.Usable;
//
//@Component
//public class RepositoryFactory {
//    private ItemRepository itemRepository;
//
//
//    @Bean
//    @Autowired
//    public ItemRepository<Showable> getShowable(ItemRepository<Showable> itemRepository) {
//        this.itemRepository = itemRepository;
//        return this.itemRepository;
//    }
//
//    @Bean
//    public ItemRepository<Usable> getUsable(ItemRepository<Usable> itemRepository) {
//        this.itemRepository = itemRepository;
//        return this.itemRepository;
//    }
//
//    @Bean
//    public ItemRepository<Serviceable> getServicable(ItemRepository<Serviceable> itemRepository) {
//        this.itemRepository = itemRepository;
//        return this.itemRepository;
//    }
//}
