package org.ieti.services;

import org.ieti.models.dto.UserDto;
import org.ieti.models.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserService {

    UserEntity save(UserDto newUser);
    Optional<UserEntity> findById(String id);
    Optional<UserEntity> findByEmail(String email);
    List<UserEntity> all();
    void deleteById(String id);
//    void update(String id, UserDto actUser);
    void update(String id ,UserDto actUser);
}
