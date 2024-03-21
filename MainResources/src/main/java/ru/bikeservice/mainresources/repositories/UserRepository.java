package ru.bikeservice.mainresources.repositories;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.bikeservice.mainresources.models.users.UserModel;
import ru.bikeservice.mainresources.repositories.abstracts.CommonRepository;

import java.util.List;

@Repository
public interface UserRepository extends CommonRepository<UserModel> {
    UserModel findByUsername(String userName);
    UserModel findByEmail(String email);
    List<UserModel> findByUsernameContainingIgnoreCase(String name);
    @Modifying
    @Query("UPDATE UserModel SET active = false WHERE active = true")
    void setNotActiveToAll();
}
