package org.ieti.demo.user;

import org.ieti.models.UserEntity;
import org.ieti.models.dto.UserDto;

import org.ieti.repository.UserRepository;
import org.ieti.services.UserService;
import org.ieti.services.impl.UserServiceMongoDBImlp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

public class TestUserController {

    @Mock
    UserRepository userRepository;

    @Test
    public void testCreateUser(){

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        UserService userService = new UserServiceMongoDBImlp();

        UserDto userDto = new UserDto();
        userDto.setName("prueba");
        userDto.setLastname("pruebaLast");
        userDto.setEmail("prueba@mail.com");
        userDto.setPassword("1234");
        userDto.setRoles(Set.of("USER"));

        UserEntity userEntity = new UserEntity();
        userEntity.setEmail("prueba@mail.com");
        userEntity.setPassword("1234");

        UserEntity resp = userService.save(userDto);

        Assertions.assertEquals(resp, userEntity);
    }
}
