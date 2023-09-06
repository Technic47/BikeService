package ru.kuznetsov.bikeService.services.modelServices;

import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.showable.Manufacturer;
import ru.kuznetsov.bikeService.repositories.modelRepositories.ManufacturerRepository;
import ru.kuznetsov.bikeService.services.abstracts.AbstractShowableService;

@Service
public class ManufacturerService extends AbstractShowableService<Manufacturer, ManufacturerRepository> {
    public ManufacturerService(ManufacturerRepository repository) {
        super(repository);
    }

//    public boolean exist(Manufacturer item) {
//        ExampleMatcher matcher = ExampleMatcher.matching()
//                .withIgnorePaths("id")
//                .withMatcher("name", ignoreCase())
//                .withMatcher("description", ignoreCase())
//                .withMatcher("picture", ignoreCase())
//                .withMatcher("link", ignoreCase());
//
//        Example<Manufacturer> toFind = Example.of(item, matcher);
//
//        return repository.exists(toFind);
//    }
}
