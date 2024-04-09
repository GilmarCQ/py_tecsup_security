package org.security.security.controller;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.security.security.aggregates.response.HttpResMessage;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    @GetMapping("/hola")
    public ResponseEntity<Object> saludoAdmin() {
        try {
            return ResponseEntity.status(200).body("Hola usuario admin");
        } catch (ExpiredJwtException ex) {
            return ResponseEntity.status(404).body(HttpResMessage.builder().message(ex.getMessage()).build());
        }
    }
}
