package org.security.security.service.impl;

import lombok.RequiredArgsConstructor;
import org.security.security.repository.UsuarioRepository;
import org.security.security.service.UsuarioService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return usuarioRepository.findByCorreo(username)
                        .orElseThrow(() -> new UsernameNotFoundException("Usuario no fue encontrado"));
            }
        };
    }
}
