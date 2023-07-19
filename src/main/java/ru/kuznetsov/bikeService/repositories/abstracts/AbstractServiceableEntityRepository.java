package ru.kuznetsov.bikeService.repositories.abstracts;

import org.springframework.data.repository.NoRepositoryBean;
import ru.kuznetsov.bikeService.models.abstracts.AbstractServiceableEntity;

@NoRepositoryBean
public interface AbstractServiceableEntityRepository<E extends AbstractServiceableEntity> extends AbstractShowableEntityRepository<AbstractServiceableEntity> {
//    List<E> findByPartEntityItem_idAndPartEntityType(Long Item_id, String Type);
}
