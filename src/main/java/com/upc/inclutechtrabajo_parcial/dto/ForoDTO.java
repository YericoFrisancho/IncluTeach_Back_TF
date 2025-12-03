package com.upc.inclutechtrabajo_parcial.dto;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ForoDTO {
    private Integer id;
    private String titulo;
    private String descripcion;
    private LocalDateTime fechaCreacion;
    private String nombreUsuario;
    private Integer usuarioId;
    private Integer categoriaId;
    private String nombreCategoria;
    private boolean notificacionesActivas;
}