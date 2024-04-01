package org.security.security.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Table(name = "tipo_documento")
@Entity
@Getter
@Setter
public class TipoDocumentoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo_documento")
    private Long id;

    @Column(name = "codigo", nullable = false, length = 3)
    private String codigo;

    @Column(name = "descripcion", nullable = false, length = 30)
    private String descripcion;

    @Column(name = "estado", nullable = false)
    private Integer estado;

    @Column(name = "usua_create", nullable = false, length = 20)
    private String usuaCreate;

    @Column(name = "date_create", nullable = false)
    private Timestamp dateCreate;

    @Column(name = "usua_modif", length = 20)
    private String usuaModif;

    @Column(name = "date_modif")
    private Timestamp dateModif;

    @Column(name = "usua_delet", length = 20)
    private String usuaDelet;

    @Column(name = "date_delet")
    private Timestamp dateDelet;
}
