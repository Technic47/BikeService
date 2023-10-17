package ru.kuznetsov.bikeService.repositories;

import org.springframework.stereotype.Repository;
import ru.bikeservice.mainresources.repositories.abstracts.CommonRepository;
import ru.kuznetsov.bikeService.models.security.VerificationToken;
import ru.kuznetsov.bikeService.models.users.UserModel;

@Repository
public interface VerificationTokenRepository extends CommonRepository<VerificationToken> {
    VerificationToken findByToken(String token);
    VerificationToken findByUser(UserModel user);
}
