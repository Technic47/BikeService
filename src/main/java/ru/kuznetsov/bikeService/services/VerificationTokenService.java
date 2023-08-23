package ru.kuznetsov.bikeService.services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.events.ResentTokenEvent;
import ru.kuznetsov.bikeService.models.security.VerificationToken;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.repositories.VerificationTokenRepository;
import ru.kuznetsov.bikeService.services.abstracts.AbstractService;

import java.util.Calendar;
import java.util.UUID;

@Service
public class VerificationTokenService extends AbstractService<VerificationToken, VerificationTokenRepository> {
    private final ApplicationEventPublisher eventPublisher;

    public VerificationTokenService(VerificationTokenRepository repository, ApplicationEventPublisher eventPublisher) {
        super(repository);
        this.eventPublisher = eventPublisher;
    }

    public UserModel findUserByTokenString(String verificationToken) {
        return repository.findByToken(verificationToken).getUser();
    }

    public VerificationToken findByTokenString(String VerificationToken) {
        return repository.findByToken(VerificationToken);
    }

    /**
     * Create new VerificationToken object and save it to DB.
     *
     * @param user  userModel for token
     * @param token string value of token
     */
    public VerificationToken createVerificationToken(UserModel user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        return repository.save(myToken);
    }

    public void createUpdateTokenEvent(HttpServletRequest request, UserModel userModel) {
        VerificationToken token = repository.findByUser(userModel);
        if (token == null) {
            String newToken = UUID.randomUUID().toString();
            token = this.createVerificationToken(userModel, newToken);
        } else {
            token = this.updateVerificationToken(token);
        }

        String appUrl =
                "https://" + request.getServerName() +
                        ":" + request.getServerPort() +
                        request.getContextPath();
        eventPublisher.publishEvent(new ResentTokenEvent(appUrl, userModel.getEmail(), token));
    }

    /**
     * Update expiryDate of token.
     *
     * @param token token for updating
     * @return updated token
     */
    public VerificationToken updateVerificationToken(VerificationToken token) {
        token.updateToken();
        return repository.save(token);
    }

    /**
     * Check existence and validity of the token
     *
     * @param token token to check
     * @return valid token
     * @throws RuntimeException
     */
    public VerificationToken findAndCheckToken(String token) throws RuntimeException {
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

    @Override
    public VerificationToken update(Long id, VerificationToken updateItem) {
        return null;
    }

    @Override
    public VerificationToken update(VerificationToken item, VerificationToken updateItem) {
        return null;
    }
}
