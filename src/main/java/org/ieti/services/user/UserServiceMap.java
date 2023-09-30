package org.ieti.services.user;

import org.ieti.models.User;
import org.ieti.models.UserDto;
import org.ieti.security.encrypt.PasswordEncryptionService;

import java.util.*;

public class UserServiceMap implements UserService {
    private final Map<String, User> userMap = new HashMap<>();
    private final PasswordEncryptionService passwordEncryptionService;

    public UserServiceMap(PasswordEncryptionService passwordEncryptionService) {
        this.passwordEncryptionService = passwordEncryptionService;
    }

    @Override
    public User save(UserDto newUser) {
        String id = String.valueOf(userMap.size() + 1);
        User user = new User(newUser, passwordEncryptionService.encrypt(newUser.getPassword()));
        userMap.put(id, user);
        user.setId(id);
        return user;
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(userMap.get(id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public List<User> all() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public void deleteById(String id) {
        userMap.remove(id);
    }

    @Override
    public User update(String id, UserDto actUser) {
        if (userMap.containsKey(id)) {
            User user = userMap.get(id);
            user.update(actUser);
            userMap.put(id, user);
            return user;
        }
        return null;
    }
}
