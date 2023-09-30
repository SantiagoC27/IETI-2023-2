package org.ieti.services.user;

import org.ieti.models.User;
import org.ieti.models.UserDto;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User save(UserDto newUser);

    Optional<User> findById(String id);
    Optional<User> findByEmail(String email);

    List<User> all();

    void deleteById(String id);

    User update(String id, UserDto actUser);
}
