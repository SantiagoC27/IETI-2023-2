package org.ieti.controllers.user.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private String email;
    private String name;
    private String lastname;
    private String password;
    private Set<String> roles;
}
