package ru.kuznetsov.emailsender.repositories;

import org.springframework.stereotype.Repository;
import ru.kuznetsov.emailsender.models.VerificationToken;
import ru.kuznetsov.emailsender.models.users.UserModel;

@Repository
public interface VerificationTokenRepository extends CommonRepository<VerificationToken> {
    VerificationToken findByToken(String token);
    VerificationToken findByUser(UserModel user);
}
