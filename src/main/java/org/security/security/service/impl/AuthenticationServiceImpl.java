package org.security.security.service.impl;

import lombok.RequiredArgsConstructor;
import org.security.security.aggregates.constansts.Constants;
import org.security.security.aggregates.dto.AuthDTO;
import org.security.security.aggregates.dto.UsuarioDTO;
import org.security.security.aggregates.mapper.UsuarioMapper;
import org.security.security.aggregates.response.ReniecResponse;
import org.security.security.entity.RolEntity;
import org.security.security.entity.Role;
import org.security.security.entity.TipoDocumentoEntity;
import org.security.security.entity.UsuarioEntity;
import org.security.security.repository.RolRepository;
import org.security.security.repository.TipoDocumentoRepository;
import org.security.security.repository.UsuarioRepository;
import org.security.security.aggregates.request.SignInRequest;
import org.security.security.aggregates.request.SignUpRequest;
import org.security.security.rest.config.ClienteReniecConfig;
import org.security.security.service.AuthenticationService;
import org.security.security.service.JWTService;
import org.security.security.utils.Util;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UsuarioRepository usuarioRepository;
    private final TipoDocumentoRepository tipoDocumentoRepository;
    private final RolRepository rolRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final ClienteReniecConfig reniec;
    private final UsuarioMapper usuarioMapper;
    public String message;

    @Override
    public UsuarioDTO signUpUser(SignUpRequest signUpRequest) throws Exception {
        UsuarioEntity usuario;
        UsuarioEntity usuarioEntity;

        //  Valida parametros
        if(!valParamsSignUpUser(signUpRequest))
            throw new Exception(message);

        //  Valida que correo no haya sido registrado anteriormente
        if (correoIsDuplicated(signUpRequest.getCorreo()))
            throw new Exception(Constants.MESS_CORREO_EXIST_ERROR);

        //  Valida que tipo y numero de documento no haya sido registrado anteriormente
        if (documentoIsDuplicated(signUpRequest.getTipoDocumento(), signUpRequest.getNroDocumento()))
            throw new Exception(Constants.MESS_DOCUMENT_EXISTS_ERROR);

        usuarioEntity = getEntitySignUp(signUpRequest, Role.USUARIO);
        usuario = usuarioRepository.save(usuarioEntity);

        return usuarioMapper.mapToDto(usuario);
    }

    @Override
    public UsuarioDTO signUpAdmin(SignUpRequest signUpRequest) throws Exception {
        UsuarioEntity usuario;
        UsuarioEntity usuarioEntity;

        //  Valida parametros
        if(!valParamsSignUpUser(signUpRequest))
            throw new Exception(message);

        //  Valida que correo no haya sido registrado anteriormente
        if (correoIsDuplicated(signUpRequest.getCorreo()))
            throw new Exception(Constants.MESS_CORREO_EXIST_ERROR);

        //  Valida que tipo y numero de documento no haya sido registrado anteriormente
        if (documentoIsDuplicated(signUpRequest.getTipoDocumento(), signUpRequest.getNroDocumento()))
            throw new Exception(Constants.MESS_DOCUMENT_EXISTS_ERROR);

        usuarioEntity = getEntitySignUp(signUpRequest, Role.USUARIO);
        usuario = usuarioRepository.save(usuarioEntity);

        return usuarioMapper.mapToDto(usuario);
    }

    @Override
    public AuthDTO signIn(SignInRequest signInRequest) {
        //  validar parametros de entrada
       // if (!valParamsSignIn(signInRequest))
         //   throw new Exception(message);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    signInRequest.getCorreo(), signInRequest.getPassword()));

        var usuario = usuarioRepository.findByCorreo(signInRequest.getCorreo())
                .orElseThrow(() -> new IllegalArgumentException("Correo, " + Constants.MESS_PARAMS_ERROR));

        var jwt = jwtService.generarToken(usuario);
        AuthDTO authDTO = AuthDTO.builder().token(jwt).build();
        return authDTO;
    }

    @Override
    public UsuarioDTO findUsuarioById(Long id) throws Exception {
        Optional<UsuarioEntity> usuarioBD = usuarioRepository.findById(id);
        if (usuarioBD.isEmpty()) throw new Exception("No se encontro el usuario con id " + id);
        return usuarioMapper.mapToDto(usuarioBD.get());
    }

    private UsuarioEntity getEntitySignUp(SignUpRequest signUpRequest, Role role) throws Exception {
        Optional<RolEntity> rolBD = rolRepository.findByDescripcion(role.name());
        if (rolBD.isEmpty()) return null;

        Optional<TipoDocumentoEntity> tipoDocBD = tipoDocumentoRepository.findByCodigo(signUpRequest.getTipoDocumento());
        if (tipoDocBD.isEmpty()) return null;

        if (signUpRequest.getTipoDocumento().equals("DNI")) {
            try {
                ReniecResponse persona = reniec.getInfoReniec(signUpRequest.getNroDocumento());
                signUpRequest.setApellidos(persona.getApellidoPaterno() + " " + persona.getApellidoMaterno());
                signUpRequest.setNombres(persona.getNombres());
            } catch (Exception ex) {
                throw new Exception("No se encontro la información de la persona al consultar con RENIEC.");
            }
        }

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setUsuario(signUpRequest.getUsuario());
        usuario.setPassword(new BCryptPasswordEncoder().encode(signUpRequest.getPassword()));
        usuario.setNombres(signUpRequest.getNombres());
        usuario.setApellidos(signUpRequest.getApellidos());
        usuario.setNroDocumento(signUpRequest.getNroDocumento());
        usuario.setDireccion(signUpRequest.getDireccion());
        usuario.setTelefono(signUpRequest.getTelefono());
        usuario.setCorreo(signUpRequest.getCorreo());
        usuario.setRol(rolBD.get());
        usuario.setTipoDocumento(tipoDocBD.get());
        usuario.setDateCreate(Util.getTimestamp());
        usuario.setUsuaCreate(signUpRequest.getUsuario());
        usuario.setEstado(1);
        return usuario;
    }

    private Boolean correoIsDuplicated(String correo) {
        Optional<UsuarioEntity> usuario = usuarioRepository.findByCorreo(correo);
        return usuario.isPresent();
    }
    private Boolean documentoIsDuplicated(String tipDocu, String numDocu) {
        Optional<TipoDocumentoEntity> tipoDocumentoBD = tipoDocumentoRepository.findByCodigo(tipDocu);
        Optional<UsuarioEntity> usuarioBD =
                usuarioRepository.findByTipoDocumentoAndNroDocumento(numDocu, tipoDocumentoBD.get().getId());
        return usuarioBD.isPresent();
    }
    private Boolean valParamsSignUpUser(SignUpRequest signUpRequest) {
        if (signUpRequest.getCorreo().isEmpty()){
            message = "Correo, " + Constants.MESS_PARAMS_ERROR;
            return false;
        }
        else if (signUpRequest.getTipoDocumento() == null || signUpRequest.getTipoDocumento().isEmpty()) {
            message = "Tipo de documento, " + Constants.MESS_PARAMS_ERROR;
            return false;
        } else if (signUpRequest.getNroDocumento() == null || signUpRequest.getNroDocumento().isEmpty()) {
            message = "Número de documento, " + Constants.MESS_PARAMS_ERROR;
            return false;
        } else if (signUpRequest.getUsuario() == null || signUpRequest.getUsuario().isEmpty()) {
            message = "Usuario, " + Constants.MESS_PARAMS_ERROR;
            return false;
        } else if (signUpRequest.getPassword() == null || signUpRequest.getPassword().isEmpty()) {
            message = "Password, " + Constants.MESS_PARAMS_ERROR;
            return false;
        } else if (!signUpRequest.getTipoDocumento().equals("DNI")) {
            if (signUpRequest.getNombres() == null || signUpRequest.getNombres().isEmpty()) {
                message = "Nombres, " + Constants.MESS_PARAMS_ERROR;
                return false;
            } else if (signUpRequest.getApellidos() == null || signUpRequest.getApellidos().isEmpty()) {
                message = "Apellidos, " + Constants.MESS_PARAMS_ERROR;
                return false;
            }
        }
        return true;
    }

    private Boolean valParamsSignIn(SignInRequest signInRequest) {
        if (signInRequest.getCorreo().isEmpty()){
            message = "Correo, " + Constants.MESS_PARAMS_ERROR;
            return false;
        }
        else if (signInRequest.getPassword() == null || signInRequest.getPassword().isEmpty()) {
            message = "Contraseña, " + Constants.MESS_PARAMS_ERROR;
            return false;
        }
        return true;
    }
}
