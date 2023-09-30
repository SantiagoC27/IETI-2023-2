package org.ieti.models;
import org.ieti.repository.RoleEnum;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

@Document(collection = "user_collection")
public class User implements UserDetails {
    @Id
    private String id;
    @Serial
    private static final long serialVersionUID = 1L;
    private Date createdAt;
    private String name;
    private String lastName;
    private String email;
    List<RoleEnum> roles;
    private String encryptedPassword;

    public User() {
    }
    public User(String id, String name, String lastName, String email, String password) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.email = email;
        this.encryptedPassword = new BCryptPasswordEncoder().encode(password);
        this.createdAt = new Date();
        roles = new ArrayList<>(Collections.singleton(RoleEnum.USER));
    }

    public User(UserDto userDto, String encryptedPassword) {
        this.id = null;
        this.name = userDto.getName();
        this.lastName = userDto.getLastName();
        this.email = userDto.getEmail();
        this.encryptedPassword = encryptedPassword;
        this.createdAt = new Date();
        roles = new ArrayList<>(Collections.singleton(RoleEnum.USER));
    }

    public List<RoleEnum> getRoles() {return roles; }

    public String getId() {
        return id;
    }

    public void setId(String id){this.id = id;}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setencryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public void update(UserDto userDto) {
        this.name = userDto.getName();
        this.lastName = userDto.getLastName();
        this.email = userDto.getEmail();
        if (!userDto.getPassword().isEmpty()) {
            this.encryptedPassword = new BCryptPasswordEncoder().encode(userDto.getPassword());
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(RoleEnum.USER.name()));
    }

    @Override
    public String getPassword() {
        return encryptedPassword;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
