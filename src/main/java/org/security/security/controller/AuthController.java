package org.security.security.controller;

import lombok.RequiredArgsConstructor;
import org.security.security.aggregates.dto.UsuarioDTO;
import org.security.security.aggregates.request.SignInRequest;
import org.security.security.aggregates.request.SignUpRequest;
import org.security.security.aggregates.response.AuthResponse;
import org.security.security.aggregates.response.BaseResponse;
import org.security.security.aggregates.response.HttpResBody;
import org.security.security.aggregates.response.HttpResMessage;
import org.security.security.service.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.http.HttpResponse;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signupuser")
    public ResponseEntity<Object> signUpUser(@RequestBody SignUpRequest signUpRequest) {
        try {
            return ResponseEntity.status(200).body(authenticationService.signUpUser(signUpRequest));
        } catch (Exception ex) {
            return ResponseEntity.status(404).body(HttpResMessage.builder().message(ex.getMessage()).build());
        }
    }

    @PostMapping("/signupadmin")
    public ResponseEntity<Object> signUpAdmin(@RequestBody SignUpRequest signUpRequest) {
        try {
            return ResponseEntity.status(200).body(authenticationService.signUpUser(signUpRequest));
        } catch (Exception ex) {
            return ResponseEntity.status(404).body(HttpResMessage.builder().message(ex.getMessage()).build());
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<Object> signIn(@RequestBody SignInRequest signInRequest) {
        try {
            return ResponseEntity.status(200).body(authenticationService.signIn(signInRequest));
        } catch (Exception ex) {
            return ResponseEntity.status(404).body(HttpResMessage.builder().message(ex.getMessage()).build());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findUsuario(@PathVariable Long id) {
        try {
            return ResponseEntity.status(200).body(authenticationService.findUsuarioById(id));
        } catch (Exception ex) {
            return ResponseEntity.status(404).body(HttpResMessage.builder().message(ex.getMessage()).build());
        }
    }

}
