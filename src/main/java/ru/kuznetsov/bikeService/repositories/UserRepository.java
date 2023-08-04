package ru.kuznetsov.bikeService.repositories;

import org.springframework.stereotype.Repository;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.repositories.abstracts.CommonRepository;

import java.util.List;

@Repository
public interface UserRepository extends CommonRepository<UserModel> {
    UserModel findByUsername(String userName);
    UserModel findByEmail(String email);
    List<UserModel> findByUsernameContainingIgnoreCase(String name);
}
