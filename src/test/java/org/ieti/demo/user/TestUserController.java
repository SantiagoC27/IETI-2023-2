package org.ieti.demo.user;

import org.bson.types.ObjectId;
import org.ieti.Server;
import org.ieti.models.RoleEntity;
import org.ieti.models.RoleEnum;
import org.ieti.models.UserEntity;
import org.ieti.models.dto.UserDto;
import org.ieti.repository.UserRepository;
import org.ieti.services.impl.UserServiceMongoDBImlp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = Server.class)
@ExtendWith(MockitoExtension.class)
public class TestUserController {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceMongoDBImlp userService;

    private UserEntity testUser;
    private UserDto testUserDto;

    @BeforeEach
    void setUp(){

        testUserDto = new UserDto();
        testUserDto.setName("Name");
        testUserDto.setLastname("Last");
        testUserDto.setEmail("test@mail.com");
        testUserDto.setPassword("1234");
        testUserDto.setRoles(Set.of("USER"));

        Set<RoleEntity> roles = testUserDto.getRoles().stream()
                .map(role -> RoleEntity.builder()
                        .name(RoleEnum.valueOf("USER"))
                        .build())
                .collect(Collectors.toSet());

        UserEntity userEntity = UserEntity.builder()
                .name("Name")
                .lastName("Last")
                .email("test@mail.com")
                .password("1234")
                .roles(roles)
                .build();

        testUser = userEntity;
    }

    @Test
    void testCreateUser(){

        when(userRepository.save(any(UserEntity.class))).thenReturn(testUser);

        UserEntity responseUser = userService.save(testUserDto);

        assertThat(responseUser).isNotNull();
        assertEquals(responseUser, testUser);

        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void testCreateUserFail() {

        when(userRepository.save(any(UserEntity.class))).thenThrow(DataIntegrityViolationException.class);

        assertThatThrownBy(() -> userService.save(testUserDto))
                .isInstanceOf(DataIntegrityViolationException.class);

        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void testGetAllUsers() {

        List<UserEntity> usersList = new ArrayList<UserEntity>();

        usersList.add(testUser);

        Set<RoleEntity> roles = testUserDto.getRoles().stream()
                .map(role -> RoleEntity.builder()
                        .name(RoleEnum.valueOf("USER"))
                        .build())
                .collect(Collectors.toSet());

        UserEntity testUser2 = UserEntity.builder()
                .name("Name2")
                .lastName("Last2")
                .email("test2@mail.com")
                .password("1234")
                .roles(roles)
                .build();
        usersList.add(testUser2);

        when(userRepository.findAll()).thenReturn(usersList);

        List<UserEntity> listResponse = userService.all();

        assertEquals(2, listResponse.size());

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetAllUsersNotFound(){
        List<UserEntity> emptyUsersList = new ArrayList<>();

        when(userRepository.findAll()).thenReturn(emptyUsersList);

        List<UserEntity> response = userService.all();

        assertEquals(new ArrayList<>(), response);

        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testFindByEmail(){

        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));

        Optional<UserEntity> responseUser = userService.findByEmail(testUserDto.getEmail());

        assertEquals(responseUser, Optional.of(testUser));

        verify(userRepository, times(1)).findByEmail(testUser.getEmail());
    }

    @Test
    void testFindByEmailNotFound(){

        doThrow(EmptyResultDataAccessException.class).when(userRepository).findByEmail(anyString());

        assertThrows(EmptyResultDataAccessException.class, () -> userService.findByEmail(testUser.getEmail()));

        verify(userRepository, times(1)).findByEmail(testUser.getEmail());
    }

    @Test
    void testUpdateUser(){

        ObjectId id = new ObjectId();

        testUserDto.setEmail("testUpdate@mail.com");

        testUser.setId(id);
        testUser.setEmail("testUpdate@mail.com");

        when(userRepository.findById(id.toString())).thenReturn(Optional.of(testUser));

        userService.update(id.toString(), testUserDto);

        assertEquals(testUserDto.getEmail(), testUser.getEmail());

        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    public void testUpdateUserNotFound() {

        String id = "1";
        testUserDto.setEmail("nuevo_email@example.com");

        when(userRepository.findById(id)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userService.update(id, testUserDto));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("Usuario con el email nuevo_email@example.com no existe.", exception.getReason());

        verify(userRepository, never()).save(any());
    }

    @Test
    void testDeleteUser(){

        String id = "1";

        userService.deleteById(id);

        verify(userRepository, times(1)).deleteById(id);
    }


    @Test
    public void testDeleteUserByIdNotFound() {

        String id = "1";

        doThrow(EmptyResultDataAccessException.class).when(userRepository).deleteById(anyString());

        assertThrows(EmptyResultDataAccessException.class, () -> userService.deleteById(id));

        verify(userRepository, times(1)).deleteById(id);
    }
}
