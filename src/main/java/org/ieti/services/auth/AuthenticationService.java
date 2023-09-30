package org.ieti.services.auth;

import org.ieti.models.UserDto;

public interface AuthenticationService {
    String register(UserDto user);

    String authenticate(UserDto user);
}
