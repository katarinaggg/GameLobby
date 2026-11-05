package org.example.sessionservice.security.service;

import io.jsonwebtoken.Claims;

public interface TokenService {
    Claims parseToken(String jwt);
}