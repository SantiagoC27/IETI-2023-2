package org.ieti.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Calendar;
import java.util.Date;
import java.util.function.Function;

import static org.ieti.utils.Constants.SECRET_KEY;
import static org.ieti.utils.Constants.TOKEN_DURATION_MINUTES;

@Component
@Slf4j
public class JwtUtils {

    // Generar token de acceso
    public String generateAccesToken(String email){
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(TOKEN_DURATION_MINUTES)))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    // Validar el token de acceso
    public  boolean isTokenValid(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return true;
        }catch (Exception e){
            log.error("Token invalido, error: ".concat(e.getMessage()));
            return false;
        }
    }

    // Obtener el username del toekn
    public String getEmailFromToken(String token){
        return getClaim(token, Claims::getSubject);
    }

    // Obtener un solo claim
    public <T> T getClaim(String token, Function<Claims, T> claimsTFunction){
        Claims claims = extractAllClaims(token);
        return claimsTFunction.apply(claims);
    }

    // Obtener todos los claims del token (o el payload que va en el JWT token)
    public Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
