package org.security.security.aggregates.mapper;

import org.modelmapper.ModelMapper;
import org.security.security.aggregates.dto.UsuarioDTO;
import org.security.security.entity.UsuarioEntity;
import org.springframework.stereotype.Service;

@Service
public class UsuarioMapper {

    private static final ModelMapper modelMapper = new ModelMapper();

    public UsuarioDTO mapToDto(UsuarioEntity entity) {
        return modelMapper.map(entity, UsuarioDTO.class);
    }

    public UsuarioEntity mapToEntity(UsuarioDTO usuarioDTO) {
        return modelMapper.map(usuarioDTO, UsuarioEntity.class);
    }

}
