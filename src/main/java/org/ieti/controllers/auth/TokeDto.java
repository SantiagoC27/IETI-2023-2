package org.ieti.controllers.auth;

import java.util.Date;

public record TokeDto(String token, Date expirationDate) {
}
