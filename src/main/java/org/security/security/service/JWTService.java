package org.security.security.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface JWTService {
    String generarToken(UserDetails userDetails);
    boolean validarToken(String token, UserDetails userDetails);
    String extractUserName(String token);
}
