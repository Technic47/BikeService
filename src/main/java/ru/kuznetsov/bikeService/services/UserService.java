package ru.kuznetsov.bikeService.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kuznetsov.bikeService.models.users.UserModel;
import ru.kuznetsov.bikeService.models.users.UserRole;
import ru.kuznetsov.bikeService.repositories.UserRepository;
import ru.kuznetsov.bikeService.services.abstracts.AbstractService;

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
        userModel.getStatus().add(UserRole.USER);
        repository.save(userModel);
        return true;
    }
}
