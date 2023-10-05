package org.ieti.controllers.auth;

import org.ieti.controllers.user.request.AuthRequest;
import org.ieti.controllers.user.request.UserDto;
import org.ieti.models.UserEntity;
import org.ieti.security.jwt.JwtUtils;
import org.ieti.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @PostMapping("/login")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtUtils.generateAccesToken(authRequest.getEmail());
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }

    @GetMapping("/helloSecured")
    public String helloSecured(){
        return "Hello World Secured";
    }
}
