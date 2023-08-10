package ru.kuznetsov.bikeService.services;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.security.VerificationToken;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.repositories.VerificationTokenRepository;
import ru.kuznetsov.bikeService.services.abstracts.AbstractService;

import java.util.Calendar;

@Service
public class VerificationTokenService extends AbstractService<VerificationToken, VerificationTokenRepository> {
    public VerificationTokenService(VerificationTokenRepository repository) {
        super(repository);
    }

    public UserModel findUserByTokenString(String verificationToken) {
        return repository.findByToken(verificationToken).getUser();
    }

    public VerificationToken findByTokenString(String VerificationToken) {
        return repository.findByToken(VerificationToken);
    }

    /**
     * Create new VerificationToken object and save it to DB.
     * @param user userModel for token
     * @param token string value of token
     */
    public void createVerificationToken(UserModel user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        repository.save(myToken);
    }

    /**
     * Update expiryDate of token for user.
     * @param userModel user for updating token
     * @return updated token
     */
    public VerificationToken updateVerificationToken(UserModel userModel) {
        VerificationToken token = repository.findByUser(userModel);
        token.updateToken();
        return repository.save(token);
    }

    /**
     * Check existence and validity of the token
     * @param token token to check
     * @return valid token
     * @throws RuntimeException
     */
    public VerificationToken findAndCheckToken(String token) throws RuntimeException{
        VerificationToken verificationToken = this.findByTokenString(token);
        if (verificationToken == null) {
            throw new AuthenticationServiceException("Token not found");
        }
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            throw new AccountExpiredException("Token expired");
        }
        return verificationToken;
    }
}
