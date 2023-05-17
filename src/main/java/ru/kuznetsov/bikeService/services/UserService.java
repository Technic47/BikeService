package ru.kuznetsov.bikeService.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.lists.UserEntity;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.models.users.UserRole;
import ru.kuznetsov.bikeService.repositories.UserRepository;
import ru.kuznetsov.bikeService.services.abstracts.AbstractService;

import static ru.kuznetsov.bikeService.models.users.UserRole.ROLE_ADMIN;
import static ru.kuznetsov.bikeService.models.users.UserRole.ROLE_USER;

@Service
public class UserService extends AbstractService<UserModel, UserRepository> {
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        super(repository);
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Create user with ROLE_USER
     *
     * @param userModel prepared user record
     * @return false if user already exists.
     */
    public boolean createUser(UserModel userModel) {
        return this.constructRecord(userModel, ROLE_USER);
    }

    /**
     * Create user with ROLE_ADMIN
     *
     * @param userModel prepared user record
     * @return false if user already exists.
     */
    public boolean createAdmin(UserModel userModel) {
        return this.constructRecord(userModel, ROLE_ADMIN);
    }

    private boolean constructRecord(UserModel userModel, UserRole role) {
        if (repository.findByUsername(userModel.getUsername()) != null) {
            return false;
        }
        userModel.setActive(true);
        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
        userModel.getStatus().add(role);
        repository.save(userModel);
        return true;
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
     * Finds user record with specified name.
     *
     * @param name name for search.
     * @return UserModel wth record from DB.
     */
    public UserModel findByName(String name) {
        return repository.findByUsername(name);
    }

    /**
     * Adds admin role to user.
     *
     * @param id id of user.
     */
    public void userToAdmin(Long id) {
        UserModel user = repository.getReferenceById(id);
        user.getStatus().add(UserRole.ROLE_ADMIN);
        repository.save(user);
    }

    /**
     * Removes admin role from user.
     *
     * @param id id of user.
     */
    public void adminToUser(Long id) {
        UserModel user = repository.getReferenceById(id);
        user.getStatus().remove(UserRole.ROLE_ADMIN);
        repository.save(user);
    }
}
