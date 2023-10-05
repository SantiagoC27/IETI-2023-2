package org.ieti.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.bson.types.ObjectId;

import org.ieti.controllers.user.request.UserDto;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "User")
public class UserEntity {

    @Id
    private ObjectId id;
    private Date createdAt;
    private String name;
    private String lastName;
    private String email;
    private String password;
    private Set<RoleEntity> roles;

    public void update(UserDto userDto) {
        setName(userDto.getName());
        setLastName(userDto.getLastname());
        setEmail(userDto.getEmail());
        setPassword(new BCryptPasswordEncoder().encode(userDto.getPassword()));
    }
}
