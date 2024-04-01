package org.security.security.repository;

import org.security.security.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    Optional<UsuarioEntity> findByCorreo(String correo);
    @Query(
            value = "SELECT * FROM usuarios u where u.estado = 1 and u.nro_documento = ?1 and id_tipo_documento = ?2",
            nativeQuery = true
    )
    Optional<UsuarioEntity> findByTipoDocumentoAndNroDocumento(String numDocu, Long idTipoDocumento);

}
