package ru.kuznetsov.emailsender.repositories;

import org.springframework.stereotype.Repository;
import ru.kuznetsov.emailsender.models.users.UserModel;

import java.util.List;

@Repository
public interface UserRepository extends CommonRepository<UserModel> {
    UserModel findByUsername(String userName);
    UserModel findByEmail(String email);
    List<UserModel> findByUsernameContainingIgnoreCase(String name);
}
