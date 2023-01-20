package ru.kuznetsov.bikeService.repositories.abstracts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import ru.kuznetsov.bikeService.models.abstracts.AbstractShowableEntity;

//@Scope("prototype")
//@Repository
@NoRepositoryBean
public interface CommonRepository<E extends AbstractShowableEntity> extends JpaRepository<E, Long> {
}
