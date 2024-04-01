package org.security.security.aggregates.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsuarioDTO {
    private Long id;
    private String usuario;
    private String nombres;
    private String apellidos;
    private String nroDocumento;
    private String correo;
}
