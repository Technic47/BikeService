package ru.kuznetsov.bikeService.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.dto.RegistrationRequestDto;
import ru.kuznetsov.bikeService.models.lists.UserEntity;
import ru.kuznetsov.bikeService.models.users.UserBuilder;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.models.users.UserRole;
import ru.kuznetsov.bikeService.repositories.UserRepository;
import ru.kuznetsov.bikeService.services.abstracts.AbstractService;

import java.util.Date;
import java.util.List;

import static ru.kuznetsov.bikeService.models.users.Provider.LOCAL;
import static ru.kuznetsov.bikeService.models.users.UserRole.ROLE_ADMIN;
import static ru.kuznetsov.bikeService.models.users.UserRole.ROLE_USER;

@Service
public class UserService extends AbstractService<UserModel, UserRepository> {
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        super(repository);
        this.passwordEncoder = passwordEncoder;
    }

    public UserModel createUser(RegistrationRequestDto dto) {
        return this.registerNewUserAccount(dto.toUserModel());
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
        if (this.findByUsernameOrNull(userModel.getUsername()) != null) {
            return null;
        }
        UserModel newUser = new UserBuilder(userModel.getName(), userModel.getEmail(), userModel.getPassword())
                .encodePassword(this.passwordEncoder).setActive(true).setCreationDate()
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
    public UserModel findByUsernameOrNull(String username) {
        return repository.findByUsername(username);
    }

    public UserModel findByEmailOrNull(String email) {
        return this.repository.findByEmail(email);
    }

    public boolean existById(Long id){
        return repository.existsById(id);
    }

    private boolean emailExist(String email) {
        return repository.findByEmail(email) != null;
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

    @Override
    public UserModel update(Long id, UserModel updateItem) {
        return null;
    }

    /**
     * Update userName or password in user record.
     *
     * @param oldItem    userRecord for changing
     * @param updateItem object with new credentials
     */
    public UserModel update(UserModel oldItem, UserModel updateItem) {
        String newName = updateItem.getUsername();
        if (!newName.isEmpty()) {
            oldItem.setUsername(newName);
        }
        String newEmail = updateItem.getEmail();
        if (!newEmail.isEmpty()) {
            oldItem.setEmail(newEmail);
        }
        if (!updateItem.getPassword().isEmpty()) {
            String newPass = updateItem.getPassword();
            oldItem.setPassword(this.passwordEncoder.encode(newPass));
        }
        oldItem.setUpdated(new Date());
        return repository.save(oldItem);
    }

    /**
     * Create user with ROLE_USER.
     *
     * @param userModel prepared user record.
     * @return false if user already exists.
     */
    public UserModel registerNewUserAccount(UserModel userModel) throws RuntimeException {
        if (emailExist(userModel.getEmail())) {
            throw new RuntimeException(
                    "В системе уже существует аккаунт с почтой: "
                            + userModel.getEmail());
        }
        return this.constructRecordAndSave(userModel, ROLE_USER);
    }

    /**
     * Create user with ROLE_USER from dto object
     *
     * @param dto prepared user record.
     * @return saved new UserModel record.
     * @throws RuntimeException if user exist.
     */
    public UserModel registerNewUserAccount(RegistrationRequestDto dto) throws RuntimeException {
        if (emailExist(dto.getEmail())) {
            throw new RuntimeException(
                    "В системе уже существует аккаунт с почтой: "
                            + dto.getEmail());
        }
        return this.constructRecordAndSave(dto.toUserModel(), ROLE_USER);
    }
}
