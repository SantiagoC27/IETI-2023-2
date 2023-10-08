package org.ieti.services.impl;

import lombok.AllArgsConstructor;

import org.ieti.exeptions.UserFoundException;
import org.ieti.models.dto.UserDto;
import org.ieti.models.RoleEntity;
import org.ieti.models.RoleEnum;
import org.ieti.models.UserEntity;
import org.ieti.repository.UserRepository;

import org.ieti.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;


@Service
public class UserServiceMongoDBImlp implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserEntity save(UserDto newUser) {

        Optional<UserEntity> exitsUser = userRepository.findByEmail(newUser.getEmail());
        if(exitsUser.isPresent()){
            throw new UserFoundException(newUser.getEmail());
        }else {
            Set<RoleEntity> roles = newUser.getRoles().stream()
                    .map(role -> RoleEntity.builder()
                            .name(RoleEnum.valueOf(role))
                            .build())
                    .collect(Collectors.toSet());

            UserEntity userEntity = UserEntity.builder()
                    .createdAt(new Date())
                    .name(newUser.getName())
                    .lastName(newUser.getLastname())
                    .email(newUser.getEmail())
                    .password(passwordEncoder.encode(newUser.getPassword()))
                    .roles(roles)
                    .build();

            return userRepository.save(userEntity);
        }
    }

    @Override
    public Optional<UserEntity> findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<UserEntity> all() {
        return this.userRepository.findAll();
    }

    @Override
    public void deleteById(String id) {
        userRepository.deleteById(id);
    }

    @Override
    public void update(String id, UserDto actUser) {
        UserEntity existedUserEntity = this.findById(id).orElse(null);
        if (existedUserEntity == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,  "User with email " + actUser.getEmail() + " doesn't exist");
        }
        existedUserEntity.update(actUser);
        this.userRepository.save(existedUserEntity);
    }
}
