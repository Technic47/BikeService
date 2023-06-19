package ru.kuznetsov.bikeService.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.lists.UserEntity;
import ru.kuznetsov.bikeService.models.users.UserBuilder;
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
        return this.constructRecordAndSave(userModel, ROLE_USER);
    }

    /**
     * Create user with ROLE_ADMIN
     *
     * @param userModel prepared user record
     */
    public void createAdmin(UserModel userModel) {
        this.constructRecordAndSave(userModel, ROLE_ADMIN);
    }

    private boolean constructRecordAndSave(UserModel userModel, UserRole role) {
        if (repository.findByUsername(userModel.getUsername()) != null) {
            return false;
        }
        UserModel newUser = new UserBuilder(userModel)
                .encodePassword(this.passwordEncoder).setActive(true)
                .setRole(role).build();
        repository.save(newUser);
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

    public void update(UserModel oldItem, UserModel updateItem) {
        String newName = updateItem.getUsername();
        if (!newName.isEmpty()) {
            oldItem.setUsername(newName);
        }
        if (!updateItem.getPassword().isEmpty()) {
            String newPass = updateItem.getPassword();
            oldItem.setPassword(this.passwordEncoder.encode(newPass));
        }
        this.save(oldItem);
    }
}
