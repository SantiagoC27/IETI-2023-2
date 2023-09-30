package org.ieti.utils;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public interface Constants {

    String CLAIMS_ROLES_KEY = "ieti_roles";
    int TOKEN_DURATION_MINUTES = 1440;

    Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
}
