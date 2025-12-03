package com.upc.inclutechtrabajo_parcial.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notificacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String descripcion;

    private Boolean visto;
    private LocalDateTime fecha;
    private Integer publicacionId;

    @ManyToOne
    @JoinColumn(name = "usuariosigueforo_id")
    private ForoQueSigueUsuario usuarioSigueForo;
}