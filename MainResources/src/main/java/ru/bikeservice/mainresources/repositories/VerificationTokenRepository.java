package ru.bikeservice.mainresources.repositories;

import org.springframework.stereotype.Repository;
import ru.bikeservice.mainresources.models.security.VerificationToken;
import ru.bikeservice.mainresources.models.users.UserModel;
import ru.bikeservice.mainresources.repositories.abstracts.CommonRepository;

@Repository
public interface VerificationTokenRepository extends CommonRepository<VerificationToken> {
    VerificationToken findByToken(String token);
    VerificationToken findByUser(UserModel user);
}
