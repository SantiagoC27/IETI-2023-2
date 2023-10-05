package org.ieti.services;

import org.ieti.controllers.user.request.UserDto;
import org.ieti.models.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserEntity save(UserDto newUser);

    Optional<UserEntity> findById(String id);
    Optional<UserEntity> findByEmail(String email);

    List<UserEntity> all();

    void deleteById(String id);

    void update(String id, UserDto actUser);
}
