package org.ieti.services;

import org.ieti.controllers.user.request.UserDto;
import org.ieti.models.RoleEnum;
import org.ieti.models.RoleEntity;
import org.ieti.models.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

public class UserServiceMap implements UserService {
    private final Map<String, UserEntity> userMap = new HashMap<>();

    @Override
    public UserEntity save(UserDto newUser) {
        String id = String.valueOf(userMap.size() + 1);

        Set<RoleEntity> roles = newUser.getRoles().stream()
                .map(role -> RoleEntity.builder()
                        .name(RoleEnum.valueOf(role))
                        .build())
                .collect(Collectors.toSet());

        UserEntity userEntity = UserEntity.builder()
                .name(newUser.getName())
                .lastName(newUser.getLastname())
                .email(newUser.getEmail())
                .password(newUser.getPassword())
                .roles(roles)
                .build();

        userMap.put(id, userEntity);
        return userEntity;
    }

    @Override
    public Optional<UserEntity> findById(String id) {
        return Optional.ofNullable(userMap.get(id));
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return null;
    }

    @Override
    public List<UserEntity> all() {
        return new ArrayList<>(userMap.values());
    }

    @Override
    public void deleteById(String id) {
        userMap.remove(id);
    }

    @Override
    public void update(String id, UserDto actUser) {
        if (userMap.containsKey(id)) {
            UserEntity userEntity = userMap.get(id);
            userEntity.update(actUser);
            userMap.put(id, userEntity);
        }
    }
}
