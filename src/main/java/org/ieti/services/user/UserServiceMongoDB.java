package org.ieti.services.user;

import org.ieti.models.User;
import org.ieti.models.UserDto;
import org.ieti.repository.UserRepository;
import org.ieti.security.encrypt.PasswordEncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
@Service
public class UserServiceMongoDB implements UserService{
    private final UserRepository userRepository;
    private final PasswordEncryptionService passwordEncryptionService;

    public UserServiceMongoDB(@Autowired UserRepository userRepository, @Autowired PasswordEncryptionService passwordEncryptionService) {
        this.userRepository = userRepository;
        this.passwordEncryptionService = passwordEncryptionService;
    }

    @Override
    public User save(UserDto newUser) {
        return userRepository.save(new User(newUser, passwordEncryptionService.encrypt(newUser.getPassword())));
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> all() {
        return this.userRepository.findAll();
    }

    @Override
    public void deleteById(String id) {
        userRepository.deleteById(id);
    }

    @Override
    public User update(String id, UserDto actUser) {
        User existedUser = this.findById(id).orElse(null);

        if (existedUser == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,  "user with id " + id + " doesn't exist");
        }

        existedUser.update(actUser);

        return this.userRepository.save(existedUser);
    }
}
