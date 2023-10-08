package org.ieti.controllers.auth;

import org.ieti.models.dto.UserDto;
import org.ieti.models.UserEntity;
import org.ieti.security.jwt.JwtUtils;
import org.ieti.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    @Autowired
    UserService userService;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    AuthenticationManager authenticationManager;

    @PostMapping(value = "/register")
    public ResponseEntity<UserEntity> register(@RequestBody UserDto userDTO) {
        URI createdUserUri = URI.create("");
        return ResponseEntity.created(createdUserUri).body(userService.save(userDTO));
    }

    @GetMapping("/helloSecured")
    public String helloSecured(){
        return "Hello World Secured";
    }
}
