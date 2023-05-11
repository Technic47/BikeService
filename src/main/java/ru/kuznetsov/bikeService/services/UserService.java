package ru.kuznetsov.bikeService.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.lists.UserEntity;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.models.users.UserRole;
import ru.kuznetsov.bikeService.repositories.UserRepository;
import ru.kuznetsov.bikeService.services.abstracts.AbstractService;

import java.util.stream.Collectors;

@Service
public class UserService extends AbstractService<UserModel, UserRepository> {
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        super(repository);
        this.passwordEncoder = passwordEncoder;
    }

    public boolean createUser(UserModel userModel) {
        return this.constructRecord(userModel, UserRole.ROLE_USER);
    }

    public boolean createAdmin(UserModel userModel) {
        return this.constructRecord(userModel, UserRole.ROLE_ADMIN);
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

    public void addCreatedItem(UserModel user, UserEntity entity) {
        user.getCreatedItems().add(entity);
        repository.save(user);
    }

    public void delCreatedItem(UserModel user, UserEntity entity) {
        user.setCreatedItems(user.getCreatedItems().stream()
                .filter(item -> !item.equals(entity)).collect(Collectors.toList()));
        repository.save(user);
    }

    public UserModel findByName(String name) {
        return repository.findByUsername(name);
    }
}
