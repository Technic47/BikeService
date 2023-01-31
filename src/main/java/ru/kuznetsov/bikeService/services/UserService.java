package ru.kuznetsov.bikeService.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.lists.UserEntity;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.models.users.UserRole;
import ru.kuznetsov.bikeService.repositories.UserRepository;
import ru.kuznetsov.bikeService.services.abstracts.AbstractService;

import java.util.List;

@Service
public class UserService extends AbstractService<UserModel, UserRepository> {
    private final PasswordEncoder passwordEncoder;

    protected UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        super(repository);
        this.passwordEncoder = passwordEncoder;
    }

    public boolean createUser(UserModel userModel) {
        if (repository.findByUsername(userModel.getUsername()) != null) {
            return false;
        }
        userModel.setActive(true);
        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
        userModel.getStatus().add(UserRole.ROLE_USER);
        repository.save(userModel);
        return true;
    }

    public void addCreatedItem(UserModel user, UserEntity entity){
        user.getCreatedItems().add(entity);
        repository.save(user);
    }

    public void delCreatedItem(UserModel user, UserEntity entity){
        List<UserEntity> entityList = user.getCreatedItems();
        entityList.remove(entity);
        user.setCreatedItems(entityList);
        repository.save(user);
    }

    public UserModel findByName(String name) {
        return repository.findByUsername(name);
    }
}
