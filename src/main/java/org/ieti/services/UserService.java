package org.ieti.services;

import org.ieti.models.User;
import org.ieti.models.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User save(User user);

    Optional<User> findById(String id);

    List<User> all();

    void deleteById(String id);

    User update(String id, UserDto actUser);
}
