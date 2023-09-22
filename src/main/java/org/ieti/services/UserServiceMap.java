package org.ieti.services;

import org.apache.catalina.UserDatabase;
import org.ieti.models.User;
import org.ieti.models.UserDto;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceMap implements UserService {
    private final Map<String, User> userMap = new HashMap<>();

    @Override
    public User save(User user) {
        String id = String.valueOf(userMap.size() + 1);
        userMap.put(id, user);
        user.setId(id);
        return user;
    }

    @Override
    public Optional<User> findById(String id) {
        return Optional.ofNullable(userMap.get(id));
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
