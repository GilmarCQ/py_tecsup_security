package org.security.security.service;

import org.security.security.aggregates.dto.AuthDTO;
import org.security.security.aggregates.dto.UsuarioDTO;
import org.security.security.aggregates.request.SignInRequest;
import org.security.security.aggregates.request.SignUpRequest;
import org.security.security.aggregates.response.AuthResponse;
import org.security.security.aggregates.response.BaseResponse;

public interface AuthenticationService {
    UsuarioDTO signUpUser(SignUpRequest signUpRequest) throws Exception;
    UsuarioDTO signUpAdmin(SignUpRequest signUpRequest) throws Exception;
    //AuthDTO signIn(SignInRequest signInRequest) throws Exception;
    AuthDTO signIn(SignInRequest signInRequest);
    UsuarioDTO findUsuarioById(Long id) throws Exception;
}
