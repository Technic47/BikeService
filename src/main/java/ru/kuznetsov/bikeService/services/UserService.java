package ru.kuznetsov.bikeService.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.lists.UserEntity;
import ru.kuznetsov.bikeService.models.security.VerificationToken;
import ru.kuznetsov.bikeService.models.users.UserBuilder;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.models.users.UserRole;
import ru.kuznetsov.bikeService.repositories.UserRepository;
import ru.kuznetsov.bikeService.repositories.VerificationTokenRepository;
import ru.kuznetsov.bikeService.services.abstracts.AbstractService;

import java.util.List;
import java.util.UUID;

import static ru.kuznetsov.bikeService.models.users.Provider.LOCAL;
import static ru.kuznetsov.bikeService.models.users.UserRole.ROLE_ADMIN;
import static ru.kuznetsov.bikeService.models.users.UserRole.ROLE_USER;

@Service
public class UserService extends AbstractService<UserModel, UserRepository> {
    private final PasswordEncoder passwordEncoder;
    private VerificationTokenRepository tokenRepository;
    private final JavaMailSender mailSender;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder, JavaMailSender mailSender) {
        super(repository);
        this.passwordEncoder = passwordEncoder;
        this.mailSender = mailSender;
    }

    /**
     * Create user with ROLE_USER
     *
     * @param userModel prepared user record
     * @return false if user already exists.
     */
    public boolean createUser(UserModel userModel) {
        return this.constructRecordAndSave(userModel, ROLE_USER) != null;
    }

    /**
     * Create user with ROLE_ADMIN
     *
     * @param userModel prepared user record
     */
    public void createAdmin(UserModel userModel) {
        this.constructRecordAndSave(userModel, ROLE_ADMIN);
    }

    private UserModel constructRecordAndSave(UserModel userModel, UserRole role) {
        if (this.findByUsername(userModel.getUsername()) != null) {
            return null;
        }
        UserModel newUser = new UserBuilder(userModel.getName(), userModel.getEmail(), userModel.getPassword())
                .encodePassword(this.passwordEncoder).setActive(true)
                .addRole(role).setProvider(LOCAL).build();
        return repository.save(newUser);
    }

    /**
     * Adds UserEntity to user`s entityList.
     *
     * @param user   user to add entity.
     * @param entity entity to add.
     */
    public void addCreatedItem(UserModel user, UserEntity entity) {
        user.getCreatedItems().add(entity);
        repository.save(user);
    }

    /**
     * Removes UserEntity from user`s entityList.
     *
     * @param user   user to remove entity.
     * @param entity entity to remove.
     */
    public void delCreatedItem(UserModel user, UserEntity entity) {
        user.getCreatedItems().remove(entity);
        repository.save(user);
    }

    /**
     * Finds user record with specified username.
     *
     * @param username name for search.
     * @return UserModel wth record from DB.
     */
    public UserModel findByUsername(String username) {
        return repository.findByUsername(username);
    }

    public UserModel findByEmail(String email) {
        return this.repository.findByEmail(email);
    }

    /**
     * Find user records containing argument username in username field.
     *
     * @param username search value
     * @return list of matching records.
     */
    public List<UserModel> findByUsernameContainingIgnoreCase(String username) {
        return repository.findByUsernameContainingIgnoreCase(username);
    }

    /**
     * Adds admin role to user.
     *
     * @param id id of user.
     */
    public void userToAdmin(Long id) {
        UserModel model = repository.findById(id).get();
        model.getAuthorities().add(ROLE_ADMIN);
        repository.save(model);
    }

    /**
     * Removes admin role from user.
     *
     * @param id id of user.
     */
    public void adminToUser(Long id) {
        UserModel model = repository.findById(id).get();
        model.getAuthorities().remove(ROLE_ADMIN);
        repository.save(model);
    }

    /**
     * Update userName or password in user record.
     *
     * @param oldItem    userRecord for changing
     * @param updateItem object with new credentials
     */
    public void update(UserModel oldItem, UserModel updateItem) {
        String newName = updateItem.getUsername();
        if (!newName.isEmpty()) {
            oldItem.setUsername(newName);
        }
        if (!updateItem.getPassword().isEmpty()) {
            String newPass = updateItem.getPassword();
            oldItem.setPassword(this.passwordEncoder.encode(newPass));
        }
        repository.save(oldItem);
    }

    public UserModel registerNewUserAccount(UserModel userModel) throws RuntimeException {
        if (emailExist(userModel.getEmail())) {
            throw new RuntimeException(
                    "There is an account with that email address: "
                            + userModel.getEmail());
        }
        return this.constructRecordAndSave(userModel, ROLE_USER);
    }

    private boolean emailExist(String email) {
        return repository.findByEmail(email) != null;
    }

    public UserModel findByToken(String verificationToken) {
        return tokenRepository.findByToken(verificationToken).getUser();
    }

    public VerificationToken getVerificationToken(String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }

    public void saveRegisteredUser(UserModel user) {
        repository.save(user);
    }

    public void createVerificationToken(UserModel user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        tokenRepository.save(myToken);
    }

    public VerificationToken generateNewVerificationToken(String existingToken){
        VerificationToken token = tokenRepository.findByToken(existingToken);
        token.updateToken();
        tokenRepository.save(token);
        return token;
    }

    public void constructSendVerificationEmail(UserModel user, String contextPath){
        String token = UUID.randomUUID().toString();
        this.createVerificationToken(user, token);

        String confirmationUrl = "/registrationConfirm?token=" + token;
        String message = "Для окончания регистрации пройдите по следующей ссылке:";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setFrom("yourbikeservice.verification@yandex.ru");
        email.setSubject("Подтверждение регистрации на yourbikeservice.");
        email.setText(message + "\r\n" + contextPath + confirmationUrl);
        mailSender.send(email);
    }

    public void constructResendVerificationTokenEmail
            (UserModel user, VerificationToken newToken, String contextPath) {
        String confirmationUrl ="/registrationConfirm?token=" + newToken.getToken();
        String message = "Для регистрации пройдите по следующей ссылке:";

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setFrom("yourbikeservice.verification@yandex.ru");
        email.setSubject("Resend Registration Token");
        email.setText(message + "\r\n" + contextPath + confirmationUrl);
        mailSender.send(email);
    }

    @Autowired
    public void setTokenRepository(VerificationTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }
}
