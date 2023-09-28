package ru.kuznetsov.emailsender.repositories;

import org.springframework.stereotype.Repository;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.repositories.abstracts.CommonRepository;
import ru.kuznetsov.emailsender.models.VerificationToken;

@Repository
public interface VerificationTokenRepository extends CommonRepository<VerificationToken> {
    VerificationToken findByToken(String token);
    VerificationToken findByUser(UserModel user);
}
