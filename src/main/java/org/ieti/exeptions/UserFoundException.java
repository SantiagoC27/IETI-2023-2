package org.ieti.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserFoundException extends ResponseStatusException {
    public UserFoundException(String email){
        super(HttpStatus.FOUND, "Usuario con el email: " + email + " ya existe");
    }
}
