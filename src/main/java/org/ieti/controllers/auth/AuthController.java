package org.ieti.controllers.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.ieti.exeptions.InvalidCredentialsException;
import org.ieti.models.User;
import org.ieti.models.UserDto;
import org.ieti.security.encrypt.PasswordEncryptionService;
import org.ieti.services.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.net.URI;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import static org.ieti.utils.Constants.*;


@RestController
@RequestMapping("v1/auth")
public class AuthController {
    private final UserService userService;
    private final PasswordEncryptionService passwordEncryptionService;

    public AuthController(@Autowired UserService userService,@Autowired PasswordEncryptionService passwordEncryptionService) {
        this.userService = userService;
        this.passwordEncryptionService = passwordEncryptionService;
    }

    @PostMapping(value = "login")
    public TokeDto login(@RequestBody LoginDto loginDto){
        Optional<User> optionalUser = userService.findByEmail(loginDto.email());
        if(optionalUser.isEmpty()){
            throw new InvalidCredentialsException();
        }

        User user = optionalUser.get();

        if(passwordEncryptionService.isPasswordMatch(loginDto.password(), user.getEncryptedPassword())){
            return generateTokenDto(user);
        }else{
            throw new InvalidCredentialsException();
        }
    }

    @PostMapping(value = "register")
    public ResponseEntity<User> register(@RequestBody UserDto userDTO) {
        URI createdUserUri = URI.create("");
        return ResponseEntity.created(createdUserUri).body(userService.save(userDTO));
    }

    private String generateToken(User user, Date expirationDate){
        return Jwts.builder()
                .setSubject(user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(SECRET_KEY)
                .compact();
    }

    private TokeDto generateTokenDto(User user){
        Calendar expirationDate = Calendar.getInstance();
        expirationDate.add(Calendar.MINUTE, TOKEN_DURATION_MINUTES);
        String token = generateToken(user, expirationDate.getTime());
        return new TokeDto(token, expirationDate.getTime());
    }

}
